package space.getlike.util_core.utils

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.autoSaver
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

@OptIn(SavedStateHandleSaveableApi::class)
inline fun <reified T> SavedStateHandle.saveableStateFlow(
    initialValue: T,
): PropertyDelegateProvider<Any?, ReadOnlyProperty<Any?, MutableStateFlow<T>>> =
    saveable(
        saver = Saver(
            save = { original -> Json.encodeToString(original.value) },
            restore = { saved -> MutableStateFlow(Json.decodeFromString(saved)) },
        ),
        init = { MutableStateFlow(initialValue) },
    )

@OptIn(SavedStateHandleSaveableApi::class)
internal fun <T> SavedStateHandle.saveableStateOf(
    initialValue: () -> T,
    serializer: KSerializer<T>,
): PropertyDelegateProvider<Any?, ReadOnlyProperty<Any?, MutableState<T>>> =
    saveable(
        saver = Saver(
            save = { original -> Json.encodeToString(serializer, original.value) },
            restore = { saved -> mutableStateOf(Json.decodeFromString(serializer, saved)) },
        ),
        init = { mutableStateOf(initialValue()) },
    )

@OptIn(SavedStateHandleSaveableApi::class)
inline fun <reified T> SavedStateHandle.saveableStateListOf(
    vararg initialValues: T,
): PropertyDelegateProvider<Any?, ReadOnlyProperty<Any?, SnapshotStateList<T>>> =
    saveable(
        saver = Saver(
            save = { original -> Json.encodeToString(original.toList()) },
            restore = { saved -> mutableStateListOf(*Json.decodeFromString<List<T>>(saved).toTypedArray()) },
        ),
        init = { mutableStateListOf(*initialValues) },
    )

@OptIn(SavedStateHandleSaveableApi::class)
inline fun <reified T> SavedStateHandle.saveableMutableListOf(
    vararg initialValues: T,
): PropertyDelegateProvider<Any?, ReadOnlyProperty<Any?, MutableList<T>>> =
    saveable(
        saver = Saver(
            save = { original -> Json.encodeToString(original) },
            restore = { saved -> mutableListOf(*Json.decodeFromString<List<T>>(saved).toTypedArray()) },
        ),
        init = { mutableListOf(*initialValues) },
    )

@OptIn(SavedStateHandleSaveableApi::class)
inline fun <reified T> SavedStateHandle.saveableMutableSetOf(
    vararg initialValues: T,
): PropertyDelegateProvider<Any?, ReadOnlyProperty<Any?, MutableSet<T>>> =
    saveable(
        saver = Saver(
            save = { original -> Json.encodeToString(original) },
            restore = { saved -> mutableSetOf(*Json.decodeFromString<List<T>>(saved).toTypedArray()) },
        ),
        init = { mutableSetOf(*initialValues) },
    )

@OptIn(SavedStateHandleSaveableApi::class)
fun <T : Any> SavedStateHandle.saveable(
    initialValue: T,
): PropertyDelegateProvider<Any?, ReadWriteProperty<Any?, T>> =
    PropertyDelegateProvider { thisRef, property ->
        var mutableState by saveable(
            stateSaver = autoSaver(),
            init = { mutableStateOf(initialValue) }
        )
        object : ReadWriteProperty<Any?, T> {
            override fun getValue(thisRef: Any?, property: KProperty<*>): T =
                mutableState

            override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
                mutableState = value
            }
        }
    }
