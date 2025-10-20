package space.getlike.util_core.utils

inline fun <T> Collection<T>.mutate(
    block: MutableList<T>.() -> Unit,
): List<T> =
    toMutableList().apply(block)

inline fun <T> Collection<T>.replaceFirstBy(
    predicate: (T) -> Boolean,
    value: T,
): List<T> {
    val index = indexOfFirst(predicate)
    return if (index == -1) {
        toList()
    } else {
        mutate { this[index] = value }
    }
}

inline fun <T> Collection<T>.addOrReplaceFirstBy(
    predicate: (T) -> Boolean,
    value: T,
): List<T> {
    val index = indexOfFirst(predicate)
    return if (index == -1) {
        toMutableList().apply { add(value) }
    } else {
        mutate { this[index] = value }
    }
}

inline fun <T> Collection<T>.replaceBy(
    predicate: (T) -> Boolean,
    block: T.() -> T,
): List<T> =
    map { item ->
        if (predicate(item)) {
            item.block()
        } else {
            item
        }
}