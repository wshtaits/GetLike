package space.getlike.client_profile_editor

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import space.getlike.client_base.presentation.design.ThemedPreview

@Preview
@Composable
fun Preview() {
    ThemedPreview(
        view = ::ProfileEditorView,
        state = ProfileEditorState(
            profile = null,
            avatarPath = null,
            name = "Angelina",
            isNameValid = true,
            isLoading = false,
            hasConnection = true,
            shouldShowImageMenu = false,
        ),
    )
}