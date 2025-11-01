package audio.recorder

import android.content.Context
import android.media.AudioDeviceCallback
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.widget.Toast
import androidx.annotation.RequiresApi

@RequiresApi(31)
class BluetoothAudioRouterModern(
    private val context: Context,
    private val audioManager: AudioManager
) : BluetoothAudioRouter {

    private var onBluetoothLost: (() -> Unit)? = null
    private var onReady: (() -> Unit)? = null
    private var currentDevice: AudioDeviceInfo? = null
    private var deviceCallback: AudioDeviceCallback? = null

    override fun enableBluetoothMic(
        onReady: () -> Unit,
        onBluetoothLost: (() -> Unit)?,
        onNoBlueToothDevice: () -> Unit
    ) {
        this.onReady = onReady
        this.onBluetoothLost = onBluetoothLost

        // Listen for future connection or disconnection events
        deviceCallback = object : AudioDeviceCallback() {
            override fun onAudioDevicesAdded(addedDevices: Array<out AudioDeviceInfo>?) {
                addedDevices?.forEach { device ->
                    if (device.type == AudioDeviceInfo.TYPE_BLUETOOTH_SCO) {
                        println("BluetoothRouterModern : Bluetooth mic connected: ${device.productName}")
                        // Automatically use the new device if not already active
                        if (audioManager.communicationDevice?.type != AudioDeviceInfo.TYPE_BLUETOOTH_SCO) {
                            currentDevice = device
                            Toast.makeText(
                                context,
                                "Bluetooth connected ${currentDevice?.productName}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

            override fun onAudioDevicesRemoved(removedDevices: Array<out AudioDeviceInfo>?) {
                removedDevices?.forEach { device ->
                    if (device.type == AudioDeviceInfo.TYPE_BLUETOOTH_SCO) {
                        println("BluetoothRouterModern : Bluetooth mic disconnected: ${device.productName}")
                        currentDevice = null
                        Toast.makeText(
                            context,
                            "Bluetooth disconnected ${device.productName}",
                            Toast.LENGTH_SHORT
                        ).show()
                        onBluetoothLost?.invoke()
                    }
                }
            }
        }

        audioManager.registerAudioDeviceCallback(deviceCallback!!, null)


        val btDevice = audioManager.availableCommunicationDevices
            .firstOrNull { it.type == AudioDeviceInfo.TYPE_BLUETOOTH_SCO }

        if (btDevice != null) {
            val success = audioManager.setCommunicationDevice(btDevice)
            if (success) {
                currentDevice = btDevice
                onReady()
            } else {
                onNoBlueToothDevice.invoke()
            }
        } else {
            onNoBlueToothDevice.invoke()
        }
    }

    override fun disableBluetoothMic() {
        audioManager.clearCommunicationDevice()
        currentDevice = null
    }

    override fun isBluetoothMicActive(): Boolean {
        return audioManager.communicationDevice?.type == AudioDeviceInfo.TYPE_BLUETOOTH_SCO
    }

    override fun release() = disableBluetoothMic()
}
