package space.getlike.client_base.presentation.design.molecules

import android.view.HapticFeedbackConstants
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView

actual class HapticProvider(
    val view: View,
)

@Composable
actual fun hapticProvider(): HapticProvider =
    HapticProvider(LocalView.current)

actual fun HapticProvider.performHaptic() {
    view.performHapticFeedback(
        HapticFeedbackConstants.LONG_PRESS,
        @Suppress("DEPRECATION") HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING,
    )
}