package space.getlike.util_image_picker

expect class ImagePicker {

    suspend fun takeWithCamera(): String?

    suspend fun pickFromGallery(): String?

    suspend fun loadFile(path: String): ByteArray?
}