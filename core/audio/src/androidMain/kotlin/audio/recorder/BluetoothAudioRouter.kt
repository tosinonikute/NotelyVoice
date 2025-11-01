package audio.recorder

interface BluetoothAudioRouter {
    /**
     * Enables routing audio to a Bluetooth mic.
     * When routing is successfully established, calls [onReady].
     * If Bluetooth disconnects later, calls [onBluetoothLost].
     */
    fun enableBluetoothMic(
        onReady: () -> Unit,
        onBluetoothLost: (() -> Unit)? = null,
        onNoBlueToothDevice: () -> Unit
    )

    /** Stops routing to Bluetooth mic (and removes any listeners). */
    fun disableBluetoothMic()

    /** Whether Bluetooth mic is currently active. */
    fun isBluetoothMicActive(): Boolean

    /** Optional cleanup. */
    fun release()
}
