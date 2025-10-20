package space.getlike.client_terms

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import space.getlike.client_base.presentation.design.ThemedPreview
import space.getlike.util_core.Example

@Preview
@Composable
fun Preview() {
    ThemedPreview(
        view = ::TermsView,
        state = TermsState(
            terms = Example.string(100),
        ),
    )
}
