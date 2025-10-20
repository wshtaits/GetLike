package space.getlike.util_permissions.handlers

import platform.AVFoundation.AVAuthorizationStatusAuthorized
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.authorizationStatusForMediaType
import platform.AVFoundation.requestAccessForMediaType
import space.getlike.util_permissions.PermissionHandler

class CameraPermissionHandler : PermissionHandler {

    override fun has(): Boolean =
        AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo) == AVAuthorizationStatusAuthorized

    override fun request(onResult: (Boolean) -> Unit) =
        AVCaptureDevice.requestAccessForMediaType(AVMediaTypeVideo, onResult)
}
