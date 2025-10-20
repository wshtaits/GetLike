package space.getlike.client_profile_editor

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil3.compose.SubcomposeAsyncImage
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import space.getlike.client_base.presentation.design.AppTheme
import space.getlike.client_base.presentation.design.atoms.DefaultAlertDialog
import space.getlike.client_base.presentation.design.atoms.DefaultAlertDialogItem
import space.getlike.client_base.presentation.design.atoms.DefaultIconButton
import space.getlike.client_base.presentation.design.atoms.DefaultLargeButton
import space.getlike.client_base.presentation.design.atoms.DefaultScaffold
import space.getlike.client_base.presentation.design.atoms.DefaultSpacer
import space.getlike.client_base.presentation.design.atoms.DefaultTextField
import space.getlike.client_base.presentation.design.atoms.DefaultTopAppBar
import space.getlike.client_base.presentation.design.atoms.buttonVisuals
import space.getlike.client_base.presentation.effects.HideKeyboardEffect
import space.getlike.util_core.View
import space.getlike.resources.*
import space.getlike.util_core.utils.blinking
import space.getlike.util_core.utils.invoke

class ProfileEditorView(bundle: Bundle) : View<ProfileEditorViewModel, ProfileEditorState>(
    bundle = bundle,
    viewModelFactory = ::ProfileEditorViewModel,
) {

    @Composable
    override fun Ui() {
        HideKeyboardEffect.handle()

        DefaultScaffold(
            modifier = Modifier
                .imePadding()
            ,
            topBar = { TopBar() },
            content = { paddingValues -> Content(paddingValues) },
        )
    }

    @Composable
    private fun TopBar() {
        DefaultTopAppBar(
            navigationIcon = {
                DefaultIconButton(
                    imageRes = Res.drawable.ic_back,
                    descriptionRes = Res.string.common_description_back,
                    onClick = { viewModel.onBackClick() },
                )
            },
        )
    }

    @Composable
    private fun Content(paddingValues: PaddingValues) {
        DropdownMenu()

        ConstraintLayout(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            val (image, spacer1, textField, spacer2, button) = createRefs()

            DefaultSpacer(
                modifier = Modifier
                    .constrainAs(spacer1) {
                        top.linkTo(parent.top)
                        bottom.linkTo(image.top)
                    }
                ,
            )

            AvatarImage(
                modifier = Modifier
                    .constrainAs(image) {
                        centerHorizontallyTo(parent)
                        top.linkTo(spacer1.bottom)
                        bottom.linkTo(textField.top)
                    }
                ,
            )

            TextFieldContainer(
                modifier = Modifier
                    .constrainAs(textField) {
                        centerHorizontallyTo(parent)
                        top.linkTo(image.bottom)
                        bottom.linkTo(spacer2.top)
                    }
                ,
            )

            DefaultSpacer(
                modifier = Modifier
                    .constrainAs(spacer2) {
                        top.linkTo(textField.bottom)
                        bottom.linkTo(button.top)
                    }
                ,
            )

            DoneButton(
                modifier = Modifier
                    .constrainAs(button) {
                        centerHorizontallyTo(parent)
                        bottom.linkTo(parent.bottom, margin = 20.dp)
                    }
                ,
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun DropdownMenu() {
        DefaultAlertDialog(
            showing = state.shouldShowImageMenu,
            items = listOf(
                DefaultAlertDialogItem(
                    textRes = Res.string.profile_editor_open_camera,
                    action = { viewModel.onCameraClick() },
                ),
                DefaultAlertDialogItem(
                    textRes = Res.string.profile_editor_open_gallery,
                    action = { viewModel.onGalleryClick() },
                ),
            ),
            onDismissRequest = { viewModel.onImageMenuDismiss() },
        )
    }

    @Composable
    private fun AvatarImage(
        modifier: Modifier,
    ) {
        SubcomposeAsyncImage(
            modifier = modifier
                .size(92.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(AppTheme.colors.placeholder)
                .clickable(
                    enabled = !state.isLoading,
                    onClick = { viewModel.onAvatarClick() }
                )
            ,
            model = state.avatarPath ?: state.profile?.avatar?.uri,
            contentScale = ContentScale.Crop,
            contentDescription = stringResource(Res.string.profile_editor_avatar),
            loading = {
                CameraImage(blinking = true)
            },
            error = {
                CameraImage(blinking = false)
            },
        )
    }

    @Composable
    private fun CameraImage(
        blinking: Boolean,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
            ,
            contentAlignment = Alignment.Center,
        ) {
            Image(
                modifier = Modifier
                    .size(42.dp)
                    .blinking(blinking)
                ,
                painter = painterResource(Res.drawable.ic_camera),
                contentDescription = stringResource(Res.string.profile_editor_avatar),
                alignment = Alignment.Center,
                contentScale = ContentScale.FillBounds,
                colorFilter = ColorFilter.tint(AppTheme.colors.primary),
            )
        }
    }

    @Composable
    private fun TextFieldContainer(
        modifier: Modifier,
    ) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(Res.string.profile_editor_name),
                color = AppTheme.colors.secondary(0.7f),
            )
            DefaultTextField(
                value = state.name ?: state.profile?.name ?: "",
                enabled = !state.isLoading,
                keyboardType = KeyboardType.Text,
                keyboardActions = KeyboardActions(
                    onDone = { viewModel.onNameDoneClick() },
                ),
                onClick = { viewModel.onNameClick() },
                onValueChange = { name -> viewModel.onNameChange(name) },
            )
        }
    }

    @Composable
    private fun DoneButton(
        modifier: Modifier,
    ) {
        DefaultLargeButton(
            modifier = modifier,
            visuals = when {
                !state.hasConnection ->
                    buttonVisuals(
                        textRes = Res.string.common_no_network,
                        color = AppTheme.colors.error,
                        enabled = false,
                    )
                state.isLoading ->
                    buttonVisuals(
                        textRes = Res.string.common_cancel,
                        loading = true,
                    )
                !state.isNameValid ->
                    buttonVisuals(
                        textRes = Res.string.profile_editor_done_button,
                        enabled = false,
                    )
                else ->
                    buttonVisuals(
                        textRes = Res.string.profile_editor_done_button,
                    )
            },
            onClick = { viewModel.onDoneClick() },
        )
    }
}
