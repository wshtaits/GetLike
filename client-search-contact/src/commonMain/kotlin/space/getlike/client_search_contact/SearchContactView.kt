package space.getlike.client_search_contact

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import space.getlike.client_base.presentation.design.AppTheme
import space.getlike.client_base.presentation.design.atoms.DefaultAnimatedAlpha
import space.getlike.client_base.presentation.design.atoms.DefaultIconButton
import space.getlike.client_base.presentation.design.atoms.DefaultLargeButton
import space.getlike.client_base.presentation.design.atoms.DefaultScaffold
import space.getlike.client_base.presentation.design.atoms.DefaultSpacer
import space.getlike.client_base.presentation.design.atoms.DefaultTextField
import space.getlike.client_base.presentation.design.atoms.DefaultTopAppBar
import space.getlike.client_base.presentation.design.atoms.buttonVisuals
import space.getlike.client_base.presentation.effects.HideKeyboardEffect
import space.getlike.resources.*
import space.getlike.util_core.View
import space.getlike.util_core.utils.invoke
import space.getlike.util_phone_number.PhoneVisualTransformation

class SearchContactView(bundle: Bundle) : View<SearchContactViewModel, SearchContactState>(
    bundle = bundle,
    viewModelFactory = ::SearchContactViewModel,
) {

    @Composable
    override fun Ui() {
        HideKeyboardEffect.handle()

        DefaultScaffold(
            modifier = Modifier
                .imePadding()
            ,
            topBar = { TopBar() },
            content = { paddingValues ->
                Content(paddingValues)
            },
        )
    }

    @Composable
    private fun TopBar() {
        DefaultTopAppBar(
            titleRes = Res.string.search_contact_title,
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
            ,
        ) {
            TextFieldColumn(
                modifier = Modifier
                    .weight(1f)
                ,
            )
            ButtonColumn()
        }
    }

    @Composable
    private fun TextFieldColumn(
        modifier: Modifier,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 44.dp)
            ,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(Res.string.search_contact_enter_phone_label),
                color = AppTheme.colors.secondary(0.7f),
                style = AppTheme.typography.labelLarge,
            )

            DefaultSpacer(height = 20.dp)

            DefaultTextField(
                value = state.phone,
                enabled = state.phoneState != SearchContactState.PhoneState.Search,
                visualTransformation = PhoneVisualTransformation(),
                keyboardType = KeyboardType.Phone,
                keyboardActions = KeyboardActions(
                    onDone = { viewModel.onPhoneDoneClick() },
                ),
                onClick = { viewModel.onPhoneClick() },
                onValueChange = { phone -> viewModel.onPhoneChange(phone) },
            )
        }
    }

    @Composable
    private fun ButtonColumn() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
            ,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            DefaultAnimatedAlpha(
                visible = state.phoneState == SearchContactState.PhoneState.NotRegistered,
            ) {
                Text(
                    text = stringResource(Res.string.search_contact_not_exist_label),
                    color = AppTheme.colors.primary,
                    style = AppTheme.typography.labelSmall,
                )
            }

            DefaultSpacer(height = 20.dp)

            DefaultLargeButton(
                visuals = when {
                    !state.hasConnection ->
                        buttonVisuals(
                            textRes = Res.string.common_no_network,
                            color = AppTheme.colors.error,
                            enabled = false,
                        )
                    state.phoneState == SearchContactState.PhoneState.Search ->
                        buttonVisuals(
                            textRes = Res.string.common_cancel,
                            loading = true,
                        )
                    state.phoneState == SearchContactState.PhoneState.NotRegistered ->
                        buttonVisuals(
                            textRes = Res.string.search_contact_invite_button,
                        )
                    state.phoneState == SearchContactState.PhoneState.Valid ->
                        buttonVisuals(
                            textRes = Res.string.search_contact_search_button,
                        )
                    else ->
                        buttonVisuals(
                            textRes = Res.string.search_contact_search_button,
                            enabled = false,
                        )
                },
                onClick = { viewModel.onActionClick() },
            )

            DefaultSpacer(height = 20.dp)
        }
    }
}