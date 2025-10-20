package space.getlike.client_login

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import space.getlike.client_base.presentation.design.ThemedPreview
import space.getlike.util_core.Example

@Preview
@Composable
fun Preview() {
    ThemedPreview(
        view = ::LoginView,
        state = LoginState(
            step = LoginState.Step.Phone,
            phone = Example.phone(),
            code = "456 678",
            isPhoneValid = true,
            isCodeValid = true,
            hasConnection = true,
            authState = LoginState.AuthState.Idle,
            secondsLeft = 32,
        ),
    )
}