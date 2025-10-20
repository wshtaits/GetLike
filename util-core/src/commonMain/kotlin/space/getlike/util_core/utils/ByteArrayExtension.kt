package space.getlike.util_core.utils

import space.getlike.util_core.models.Mime

val ByteArray.mime: Mime?
    get() = Mime.fromByteArray(this)

fun ByteArray.startsWith(prefix: ByteArray): Boolean =
    take(prefix.size).toByteArray().contentEquals(prefix)