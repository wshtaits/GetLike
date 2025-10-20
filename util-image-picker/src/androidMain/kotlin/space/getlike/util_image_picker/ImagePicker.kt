package space.getlike.util_image_picker

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import kotlin.coroutines.resume
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import androidx.core.net.toUri
import java.io.ByteArrayOutputStream
import kotlin.math.max
import kotlin.math.roundToInt
import androidx.core.graphics.scale

actual class ImagePicker(
    private val activity: ComponentActivity,
) {

    @OptIn(ExperimentalUuidApi::class)
    actual suspend fun takeWithCamera(): String? =
        suspendCancellableCoroutine { continuation ->
            val picturesDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val pictureFile = File.createTempFile(
                /* prefix = */ Uuid.random().toString(),
                /* suffix = */ ".jpg",
                /* directory = */ picturesDir,
            )
            val pictureUri = FileProvider.getUriForFile(
                /* context = */ activity,
                /* authority = */ "${activity.packageName}.fileprovider",
                /* file = */ pictureFile,
            )
            activity.activityResultRegistry
                .register(
                    key = KEY_TAKE_WITH_CAMERA,
                    contract = ActivityResultContracts.TakePicture(),
                    callback = { isSuccess ->
                        val result = pictureUri
                            ?.takeIf { isSuccess }
                            ?.toString()
                        continuation.resume(result)
                    },
                )
                .launch(pictureUri)
        }

    actual suspend fun pickFromGallery(): String? =
        suspendCancellableCoroutine { continuation ->
            activity.activityResultRegistry
                .register(
                    key = KEY_PICK_FROM_GALLERY,
                    contract = ActivityResultContracts.GetContent(),
                    callback = { uri ->
                        val result = uri?.toString()
                        continuation.resume(result)
                    },
                )
                .launch("image/*")
        }

    actual suspend fun loadFile(path: String): ByteArray? =
        try {
            activity.contentResolver
                .openInputStream(path.toUri())
                ?.use { stream -> resizeImage(stream.readBytes()) }
        } catch (_: Exception) {
            null
        }

    private fun resizeImage(
        imageBytes: ByteArray,
    ): ByteArray? {
        val originalBitmap = BitmapFactory.decodeByteArray(
            /* data = */ imageBytes,
            /* offset = */ 0,
            /* length = */ imageBytes.size,
        ) ?: return null

        val width = originalBitmap.width
        val height = originalBitmap.height

        val scale = (IMAGE_MAX_SIZE / max(width, height)).coerceAtMost(1f)

        val resizedWidth = (width * scale).roundToInt()
        val resizedHeight = (height * scale).roundToInt()

        val resizedBitmap = originalBitmap.scale(resizedWidth, resizedHeight)
        val outputStream = ByteArrayOutputStream()
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, IMAGE_QUALITY, outputStream)
        return outputStream.toByteArray()
    }

    private companion object {
        const val KEY_TAKE_WITH_CAMERA = "takeWithCamera"
        const val KEY_PICK_FROM_GALLERY = "pickFromGallery"

        const val IMAGE_MAX_SIZE = 1024f
        const val IMAGE_QUALITY = 85
    }
}
