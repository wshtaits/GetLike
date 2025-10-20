package space.getlike.client_main

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import space.getlike.client_base.presentation.design.ThemedPreview

@Preview
@Composable
fun Preview() {
    ThemedPreview(
        view = ::MainView,
        state = MainState(
            currentScreen = MainState.ChildScreen.Profile,
            hasUnreadMessages = true,
        ),
    )
}