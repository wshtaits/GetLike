package space.getlike.util_core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
fun <T> rememberMutableStateOf(value: T): MutableState<T> =
    remember { mutableStateOf(value) }

@Composable
fun <T> rememberSaveableMutableStateOf(value: T): MutableState<T> =
    rememberSaveable { mutableStateOf(value) }

@Composable
fun rememberMutableIntStateOf(value: Int): MutableIntState =
    remember { mutableIntStateOf(value) }

@Composable
fun <T> rememberDerivedStateOf(calculation: () -> T): State<T> =
    remember { derivedStateOf(calculation) }

@Composable
fun <T> rememberDerivedStateOf(
    key1: Any?,
    key2: Any?,
    calculation: () -> T,
): State<T> =
    remember(key1, key2) { derivedStateOf(calculation) }