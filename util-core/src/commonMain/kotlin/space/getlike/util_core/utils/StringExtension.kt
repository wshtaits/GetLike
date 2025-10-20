package space.getlike.util_core.utils

fun String?.nullIfEmpty(): String? =
    if (this?.isNotEmpty() == true) {
        this
    } else {
        null
    }