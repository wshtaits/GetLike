package space.getlike.client_base.presentation.effects

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import space.getlike.util_core.View

object HideKeyboardEffect {

    @Suppress("ComposableNaming")
    @Composable
    context(view: View<*, *>)
    fun handle() {
        val keyboardController = LocalSoftwareKeyboardController.current
        view.Handle<HideKeyboardEffect> {
            keyboardController?.hide()
        }
    }
}