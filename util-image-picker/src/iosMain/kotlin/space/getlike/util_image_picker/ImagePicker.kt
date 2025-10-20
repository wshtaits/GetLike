package space.getlike.util_image_picker

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.get
import kotlinx.cinterop.reinterpret
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSData
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.NSUUID
import platform.Foundation.dataWithContentsOfURL
import platform.Foundation.writeToURL
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerOriginalImage
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UIImagePickerControllerSourceType.*
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.UIKit.UIViewController
import platform.darwin.NSObject
import kotlin.coroutines.resume

actual class ImagePicker(
    private val viewController: UIViewController,
) {

    actual suspend fun takeWithCamera(): String? =
        presentImagePicker(UIImagePickerControllerSourceTypeCamera)

    actual suspend fun pickFromGallery(): String? =
        presentImagePicker(UIImagePickerControllerSourceTypePhotoLibrary)

    actual suspend fun loadFile(path: String): ByteArray? =
        try {
            val url = NSURL.fileURLWithPath(path)
            val data = NSData.dataWithContentsOfURL(url) ?: return null
            ByteArray(data.length.toInt()) { index ->
                data.bytes?.reinterpret<ByteVar>()?.get(index) ?: 0
            }
        } catch (_: Exception) {
            null
        }

    private suspend fun presentImagePicker(type: UIImagePickerControllerSourceType) =
        suspendCancellableCoroutine { continuation ->
            viewController.presentViewController(
                viewControllerToPresent = UIImagePickerController().apply {
                    sourceType = type
                    allowsEditing = false
                    delegate = object : NSObject(),
                        UIImagePickerControllerDelegateProtocol,
                        UINavigationControllerDelegateProtocol {

                        override fun imagePickerController(
                            picker: UIImagePickerController,
                            didFinishPickingMediaWithInfo: Map<Any?, *>,
                        ) {
                            val image = didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage] as? UIImage
                            if (image == null) {
                                continuation.resume(null)
                                return
                            }

                            val data = UIImageJPEGRepresentation(image, 0.85)
                            if (data == null) {
                                continuation.resume(null)
                                return
                            }

                            val path = NSTemporaryDirectory() + NSUUID().UUIDString + ".jpg"
                            val url = NSURL.fileURLWithPath(path)
                            data.writeToURL(url = url, atomically = true)

                            picker.dismissViewControllerAnimated(flag = true, completion =  null)
                            continuation.resume(path)
                        }

                        override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
                            picker.dismissViewControllerAnimated(flag = true, completion = null)
                            continuation.resume(null)
                        }
                    }
                },
                animated = true,
                completion = null,
            )
        }
}
