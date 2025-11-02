package audio.launcher

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class AndroidVideoPickerLauncher(
    private val activity: ComponentActivity
) {
    fun launch(onResult: (Uri?) -> Unit) {
        val launcher: ActivityResultLauncher<Intent> = activity.activityResultRegistry.register(
            "pick_video",
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val uri = result.data?.data
            if (result.resultCode == Activity.RESULT_OK && uri != null) {
                activity.lifecycleScope.launch {
                    onResult(uri)
                }
            } else {
                onResult(null)
            }
        }

        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "video/*"
            addCategory(Intent.CATEGORY_OPENABLE)
        }

        launcher.launch(Intent.createChooser(intent, "Select Video File"))
    }
}
