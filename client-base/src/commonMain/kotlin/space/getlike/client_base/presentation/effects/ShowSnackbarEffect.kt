package space.getlike.client_base.presentation.effects

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import space.getlike.util_core.View

@ConsistentCopyVisibility
data class ShowSnackbarEffect private constructor(
    val content: Content,
) {

    constructor(text: String) : this(Content.Text(text))

    constructor(textRes: StringResource) : this(Content.Res(textRes))

    companion object {

        @Suppress("ComposableNaming")
        @Composable
        context(view: View<*, *>)
        fun handle(): SnackbarHostState {
            val snackbarHostState = remember { SnackbarHostState() }
            view.Handle<ShowSnackbarEffect> {
                val text = when (content) {
                    is Content.Text -> content.text
                    is Content.Res -> getString(content.textRes)
                }
                snackbarHostState.showSnackbar(text)
            }
            return snackbarHostState
        }
    }

    sealed interface Content {
        data class Text(val text: String) : Content
        data class Res(val textRes: StringResource) : Content
    }
}