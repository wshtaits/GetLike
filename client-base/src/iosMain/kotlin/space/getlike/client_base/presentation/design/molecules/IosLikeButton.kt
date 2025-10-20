package space.getlike.client_base.presentation.design.molecules

import androidx.compose.runtime.Composable
import platform.UIKit.UIImpactFeedbackGenerator
import platform.UIKit.UIImpactFeedbackStyle

actual class HapticProvider

@Composable
actual fun hapticProvider(): HapticProvider =
    HapticProvider()

actual fun HapticProvider.performHaptic() {
    val generator = UIImpactFeedbackGenerator(UIImpactFeedbackStyle.UIImpactFeedbackStyleMedium)
    generator.prepare()
    generator.impactOccurred()
}