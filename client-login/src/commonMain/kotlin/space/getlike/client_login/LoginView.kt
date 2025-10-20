package space.getlike.client_login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import org.jetbrains.compose.resources.stringResource
import space.getlike.client_base.presentation.design.AppTheme
import space.getlike.client_base.presentation.design.atoms.DefaultIconButton
import space.getlike.client_base.presentation.design.atoms.DefaultLargeButton
import space.getlike.client_base.presentation.design.atoms.DefaultScaffold
import space.getlike.client_base.presentation.design.atoms.DefaultSpacer
import space.getlike.client_base.presentation.design.atoms.DefaultTextButton
import space.getlike.client_base.presentation.design.atoms.DefaultTextField
import space.getlike.client_base.presentation.design.atoms.DefaultTopAppBar
import space.getlike.client_base.presentation.design.atoms.buttonVisuals
import space.getlike.client_base.presentation.design.other.CodeVisualTransformation
import space.getlike.client_base.presentation.design.other.styledStringArrayResource
import space.getlike.client_base.presentation.effects.HideKeyboardEffect
import space.getlike.client_base.presentation.effects.ScrollPagerEffect
import space.getlike.util_core.View
import space.getlike.resources.*
import space.getlike.util_phone_number.PhoneVisualTransformation

class LoginView(bundle: Bundle) : View<LoginViewModel, LoginState>(
    bundle = bundle,
    viewModelFactory = ::LoginViewModel,
) {

    @Composable
    override fun Ui() {
        HideKeyboardEffect.handle()
        val pagerState = ScrollPagerEffect.handle(pageCount = LoginState.Step.entries.size)

        DefaultScaffold(
            modifier = Modifier
                .imePadding()
            ,
            topBar = { TopBar() },
            content = { paddingValues ->
                Content(
                    paddingValues = paddingValues,
                    pagerState = pagerState,
                )
            }
        )
    }

    @Composable
    fun TopBar() {
        DefaultTopAppBar(
            navigationIcon = {
                if (state.step == LoginState.Step.Code) {
                    DefaultIconButton(
                        imageRes = Res.drawable.ic_back,
                        descriptionRes = Res.string.common_description_back,
                        onClick = { viewModel.onBackClick() },
                    )
                }
            }
        )
    }

    @Composable
    fun Content(
        paddingValues: PaddingValues,
        pagerState: PagerState,
    ) {
        HorizontalPager(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
            ,
            state = pagerState,
            userScrollEnabled = false,
        ) { page ->
            when (page) {
                LoginState.Step.Phone.number -> PhonePage()
                LoginState.Step.Code.number -> CodePage()
            }
        }
    }

    @Composable
    fun PhonePage() {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 20.dp)
            ,
        ) {
            val (label, textField, footer) = createRefs()

            Text(
                modifier = Modifier
                    .constrainAs(label) {
                        centerHorizontallyTo(parent)
                        bottom.linkTo(textField.top, margin = 20.dp)
                    }
                ,
                text = stringResource(Res.string.login_enter_phone_number_label),
                color = AppTheme.colors.secondary,
                style = AppTheme.typography.labelLarge,
            )

            DefaultTextField(
                modifier = Modifier
                    .constrainAs(textField) {
                        centerTo(parent)
                    }
                ,
                value = state.phone,
                enabled = state.authState == LoginState.AuthState.Idle,
                visualTransformation = PhoneVisualTransformation(),
                keyboardType = KeyboardType.Phone,
                keyboardActions = KeyboardActions(
                    onDone = { viewModel.onPhoneDoneClick() },
                ),
                onValueChange = { phone -> viewModel.onPhoneChange(phone) },
                onClick = { viewModel.onPhoneClick() },
            )

            Column(
                modifier = Modifier
                    .constrainAs(footer) {
                        centerHorizontallyTo(parent)
                        bottom.linkTo(parent.bottom)
                    }
                ,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                DefaultTextButton(
                    text = styledStringArrayResource(
                        res = Res.array.login_terms_label,
                        style = SpanStyle(
                            color = AppTheme.colors.primary,
                            textDecoration = TextDecoration.Underline,
                        ),
                    ),
                    onClick = { viewModel.onTermsClick() },
                )
                DefaultSpacer(height = 10.dp)
                DefaultLargeButton(
                    visuals = when {
                        !state.hasConnection ->
                            buttonVisuals(
                                textRes = Res.string.common_no_network,
                                color = AppTheme.colors.error,
                                enabled = false,
                            )
                        state.authState == LoginState.AuthState.Login ->
                            buttonVisuals(
                                textRes = Res.string.common_cancel,
                                loading = true,
                            )
                        !state.isPhoneValid ->
                            buttonVisuals(
                                textRes = Res.string.login_next_button,
                                enabled = false,
                            )
                        else ->
                            buttonVisuals(
                                textRes = Res.string.login_next_button,
                            )
                    },
                    onClick = { viewModel.onNextClick() },
                )
            }
        }
    }

    @Composable
    fun CodePage() {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 20.dp)
            ,
        ) {
            val (label, textField, statusContainer, footer) = createRefs()

            Text(
                modifier = Modifier
                    .constrainAs(label) {
                        centerHorizontallyTo(parent)
                        bottom.linkTo(textField.top, margin = 20.dp)
                    }
                ,
                text = stringResource(Res.string.login_enter_code_label),
                color = AppTheme.colors.secondary,
                style = AppTheme.typography.labelLarge.copy(textAlign = TextAlign.Center),
            )

            DefaultTextField(
                modifier = Modifier
                    .constrainAs(textField) {
                        centerTo(parent)
                    }
                ,
                value = state.code,
                keyboardType = KeyboardType.Number,
                enabled = state.authState == LoginState.AuthState.Idle,
                error = !state.isCodeValid,
                visualTransformation = CodeVisualTransformation(),
                onValueChange = { code -> viewModel.onCodeChange(code) },
                onClick = { viewModel.onCodeClick() },
            )

            Column(
                modifier = Modifier
                    .constrainAs(statusContainer) {
                        centerHorizontallyTo(parent)
                        top.linkTo(textField.bottom)
                    }
                ,
            ) {
                when {
                    state.authState == LoginState.AuthState.Verification -> {
                        DefaultSpacer(height = 30.dp)
                        CircularProgressIndicator()
                    }
                    !state.hasConnection || !state.isCodeValid -> {
                        DefaultSpacer(height = 24.dp)
                        Text(
                            text = stringResource(
                                if (!state.hasConnection) {
                                    Res.string.common_no_network
                                } else {
                                    Res.string.login_incorrect_code
                                }
                            ),
                            color = AppTheme.colors.error,
                            style = AppTheme.typography.labelSmall,
                        )
                    }
                }
            }

            DefaultTextButton(
                modifier = Modifier
                    .constrainAs(footer) {
                        centerHorizontallyTo(parent)
                        bottom.linkTo(parent.bottom)
                    }
                ,
                contentPadding = PaddingValues(
                    horizontal = 32.dp,
                    vertical = 16.dp,
                ),
                text = buildAnnotatedString {
                    append(stringResource(Res.string.login_send_sms_label))
                    if (state.secondsLeft != 0L) {
                        append(
                            AnnotatedString(
                                stringResource(
                                    Res.string.login_send_sms_seconds_label,
                                    state.secondsLeft,
                                ),
                                SpanStyle(color = AppTheme.colors.tertiary),
                            ),
                        )
                    }
                },
                enabled = state.authState == LoginState.AuthState.Idle
                        && state.hasConnection
                        && state.secondsLeft == 0L,
                loading = state.authState == LoginState.AuthState.Login,
                onClick = { viewModel.onSendSmsClick() },
            )
        }
    }
}