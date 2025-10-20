package space.getlike.utils

fun Int.separatedByThreeChars(): String =
    toString()
        .reversed()
        .chunked(3)
        .joinToString(separator = "\u00A0")
        .reversed()