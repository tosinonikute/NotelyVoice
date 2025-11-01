package audio.recorder

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothHeadset
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat

class BluetoothAudioRouterLegacy(
    private val context: Context,
    private val audioManager: AudioManager
) : BluetoothAudioRouter {

    private var onBluetoothLost: (() -> Unit)? = null
    private var onNoBlueToothDevice: (() -> Unit)? = null
    private var onReady: (() -> Unit)? = null
    private var receiverRegistered = false

    private val scoReceiver = object : BroadcastReceiver() {
        @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
        override fun onReceive(ctx: Context?, intent: Intent?) {
            val state = intent?.getIntExtra(AudioManager.EXTRA_SCO_AUDIO_STATE, -1)
            when (state) {
                AudioManager.SCO_AUDIO_STATE_CONNECTED -> {
                    println("Bluetooth SCO connected")
                    Toast.makeText(
                        context,
                        "Bluetooth connected ",
                        Toast.LENGTH_SHORT
                    ).show()
                    onReady?.invoke()
                }

                AudioManager.SCO_AUDIO_STATE_DISCONNECTED -> {
                    println("Bluetooth SCO disconnected")
                    onBluetoothLost?.invoke()
                    disableBluetoothMic()
                }
            }
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    override fun enableBluetoothMic(
        onReady: () -> Unit,
        onBluetoothLost: (() -> Unit)?,
        onNoBlueToothDevice: () -> Unit
    ) {
        this.onReady = onReady
        this.onBluetoothLost = onBluetoothLost
        this.onNoBlueToothDevice = onNoBlueToothDevice

        // ✅ Check Bluetooth device availability first
        val adapter = BluetoothAdapter.getDefaultAdapter()
        val isBtConnected = adapter?.getProfileConnectionState(BluetoothHeadset.HEADSET) ==
                BluetoothAdapter.STATE_CONNECTED

        if (!isBtConnected) {
            println("Bluetooth is not connected")
            onNoBlueToothDevice?.invoke()
            return
        }

        if (!receiverRegistered) {
            ContextCompat.registerReceiver(
                context,
                scoReceiver,
                IntentFilter(AudioManager.ACTION_SCO_AUDIO_STATE_UPDATED),
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
            receiverRegistered = true
        }

        if (!audioManager.isBluetoothScoOn) {
            audioManager.startBluetoothSco()
            audioManager.isBluetoothScoOn = true
        } else {
            onReady()
        }
    }

    override fun disableBluetoothMic() {
        try {
            audioManager.stopBluetoothSco()
            audioManager.isBluetoothScoOn = false
        } catch (_: Exception) {
        }
        if (receiverRegistered) {
            context.unregisterReceiver(scoReceiver)
            receiverRegistered = false
        }
    }

    override fun isBluetoothMicActive(): Boolean = audioManager.isBluetoothScoOn
    override fun release() = disableBluetoothMic()
}

