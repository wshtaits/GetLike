package space.getlike.util_core.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun Dp.toSp() = with(LocalDensity.current) { this@toSp.toSp() }

@Composable
fun Dp.toPx() = with(LocalDensity.current) { this@toPx.toPx() }