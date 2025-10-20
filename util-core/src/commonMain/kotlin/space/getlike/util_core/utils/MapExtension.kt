package space.getlike.util_core.utils

val <K, V> Map<K, V>.keysAndValues: Pair<Set<K>, List<V>>
    get() = keys to values.toList()