package space.getlike.util_core.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

inline fun <T> MutableStateFlow<List<T>>.replaceFirstBy(
    predicate: (T) -> Boolean,
    value: T,
) = update { collection -> collection.replaceFirstBy(predicate, value) }

inline fun <T> MutableStateFlow<List<T>>.addOrReplaceFirstBy(
    predicate: (T) -> Boolean,
    value: T,
) = update { collection -> collection.addOrReplaceFirstBy(predicate, value) }

inline fun <T> MutableStateFlow<List<T>>.replaceBy(
    predicate: (T) -> Boolean,
    crossinline block: T.() -> T,
) = update { collection -> collection.replaceBy(predicate, block) }

fun <T> MutableStateFlow<List<T>>.add(item: T) =
    update { collection -> collection + item }

fun <T> MutableStateFlow<List<T>>.add(items: List<T>) =
    update { collection -> collection + items }

fun <T> MutableStateFlow<List<T>>.remove(item: T) =
    update { collection -> collection - item }

fun <T> MutableStateFlow<List<T>>.removeAndAdd(remove: T, add: T) =
    update { collection -> collection - remove + add }