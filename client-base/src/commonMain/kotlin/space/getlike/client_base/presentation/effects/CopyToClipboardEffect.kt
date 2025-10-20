package space.getlike.client_base.presentation.effects

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.Clipboard
import androidx.compose.ui.platform.LocalClipboard
import space.getlike.util_core.View

data class CopyToClipboardEffect(
    val text: String,
) {

    companion object {

        @Suppress("ComposableNaming")
        @Composable
        context(view: View<*, *>)
        fun handle() {
            val clipboard = LocalClipboard.current
            view.Handle<CopyToClipboardEffect> {
                clipboard.setText(text)
            }
        }
    }
}

internal expect suspend fun Clipboard.setText(text: String)