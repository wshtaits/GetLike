package space.getlike.util_core.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity

@Composable
fun Float.toDp() = with(LocalDensity.current) { this@toDp.toDp() }