package space.getlike.client_search_contact

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import space.getlike.client_base.presentation.design.ThemedPreview
import space.getlike.util_core.Example

@Preview
@Composable
fun Preview() {
    ThemedPreview(
        view = ::SearchContactView,
        state = SearchContactState(
            phone = Example.phone(),
            phoneState = SearchContactState.PhoneState.Invalid,
            hasConnection = true,
        ),
    )
}