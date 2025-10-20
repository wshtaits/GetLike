package space.getlike.client_base.presentation.effects

import androidx.compose.ui.platform.Clipboard
import platform.UIKit.UIPasteboard

actual suspend fun Clipboard.setText(text: String) {
    UIPasteboard.generalPasteboard().string = text
}
