package space.getlike.util_core.models

import space.getlike.util_core.utils.startsWith

enum class Mime(
    val stringValue: String,
    val fileExtension: String,
    internal val check: (ByteArray) -> Boolean
) {

    Jpeg(
        stringValue = "image/jpeg",
        fileExtension = "jpg",
        check = { bytes ->
            bytes.size >= 3
                    && bytes.startsWith(byteArrayOf(0xFF.toByte(), 0xD8.toByte(), 0xFF.toByte()))
        },
    ),

    Png(
        stringValue = "image/png",
        fileExtension = "png",
        check = { bytes ->
            bytes.size >= 8
                    && bytes.startsWith(byteArrayOf(0x89.toByte(), 0x50, 0x4E, 0x47))
        },
    ),

    Webp(
        stringValue = "image/webp",
        fileExtension = "webp",
        check = { bytes ->
            bytes.size >= 12
                    && bytes.startsWith("RIFF".encodeToByteArray())
                    && bytes.copyOfRange(8, 12).contentEquals("WEBP".encodeToByteArray())
        }
    ),

    ;

    companion object {

        fun fromByteArray(bytes: ByteArray): Mime? =
            entries.find { entry -> entry.check(bytes) }
    }
}