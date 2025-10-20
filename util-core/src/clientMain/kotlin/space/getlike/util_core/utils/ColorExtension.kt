package space.getlike.util_core.utils

import androidx.compose.ui.graphics.Color

operator fun Color.invoke(alpha: Float): Color =
    copy(alpha = alpha)