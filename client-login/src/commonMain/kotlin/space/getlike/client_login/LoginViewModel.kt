package space.getlike.client_login

import kotlinx.coroutines.Job
import space.getlike.client_base.presentation.effects.ScrollPagerEffect
import space.getlike.client_base.presentation.routes.LoginRoute
import space.getlike.client_base.presentation.utils.countdownFlow
import space.getlike.client_base.presentation.viewmodel.ViewModel
import space.getlike.client_base.presentation.routes.TermsRoute
import space.getlike.util_core.utils.cancelAndIfNotFinished
import space.getlike.util_core.utils.isNullOrCompleted

class LoginViewModel(
    bundle: Bundle,
) : ViewModel<LoginState, LoginRoute>(
    bundle,
    analyticsName = "Login",
    initialState = { deps, _ ->
        LoginState(
            step = LoginState.Step.Phone,
            phone = "",
            code = "",
            isPhoneValid = false,
            isCodeValid = true,
            hasConnection = deps.connection.isConnectedFlow.value,
            authState = LoginState.AuthState.Idle,
            secondsLeft = 0,
        )
    },
) {

    private var countdownJob: Job? = null
    private var authenticateJob: Job? = null
    private var verifyCodeJob: Job? = null

    init {
        if (state.secondsLeft != 0L) {
            relaunchCountdown()
        }

        deps.connection.isConnectedFlow.launchCollect { isConnected ->
            state = state.copy(
                hasConnection = isConnected,
            )
            tryVerifyCode()
        }
    }

    override fun onSystemBackClick() = eventOnSystemBack {
        navigatePreviousStepOrBack()
    }

    fun onBackClick() = event("Back", Button, Click) {
        navigatePreviousStepOrBack()
    }

    fun onTermsClick() = event("Terms", Button, Click) {
        navigateRoot(TermsRoute())
    }

    fun onPhoneClick() = event("Phone", Field, Click) {
        // no op
    }

    fun onPhoneChange(phone: String) = event("Phone", Field, Change) {
        state = state.copy(
            phone = phone,
            isPhoneValid = deps.phoneFormatter.isValid(phone),
        )
    }

    fun onPhoneDoneClick() = event("PhoneDone", KeyboardButton, Click) {
        tryLogin()
    }

    fun onNextClick() = event("Next", Button, Click) {
        if (authenticateJob.isNullOrCompleted) {
            tryLogin()
        } else {
            authenticateJob?.cancelAndIfNotFinished {
                state = state.copy(
                    authState = LoginState.AuthState.Idle,
                )
            }
        }
    }

    fun onCodeClick() = event("Code", Field, Click) {
        // no op
    }

    fun onCodeChange(code: String) = event("Code", Field, Change) {
        state = state.copy(
            code = code,
            isCodeValid = true,
        )
        tryVerifyCode()
    }

    fun onSendSmsClick() = event("SendSms", Button, Click) {
        state = state.copy(
            code = "",
            isCodeValid = true,
        )
        tryLogin()
    }

    private fun tryLogin() {
        if (!state.isPhoneValid || !state.hasConnection) {
            return
        }

        state = state.copy(
            authState = LoginState.AuthState.Login,
        )

        authenticateJob = launch {
            val isLogged = deps.loginRepository.login(state.phone).getOrNull()
            if (isLogged == null) {
                state = state.copy(
                    authState = LoginState.AuthState.Idle,
                )
                return@launch
            }

            if (!isLogged) {
                state = state.copy(
                    step = LoginState.Step.Code,
                    authState = LoginState.AuthState.Idle,
                )
                relaunchCountdown()
                perform(ScrollPagerEffect(LoginState.Step.Code.number))
                return@launch
            }

            trySyncAndNavigateBack()
        }
    }

    private fun tryVerifyCode() {
        if (state.code.length != 6 || !state.hasConnection) {
            return
        }

        state = state.copy(
            authState = LoginState.AuthState.Verification,
        )

        verifyCodeJob = launch {
            val verifyResult = deps.loginRepository.verifyCode(state.code)
            if (verifyResult.isFailure) {
                state = state.copy(
                    authState = LoginState.AuthState.Idle,
                    isCodeValid = false,
                )
                return@launch
            }

            trySyncAndNavigateBack()
        }
    }

    private fun relaunchCountdown() {
        countdownJob?.cancel()
        countdownJob = countdownFlow(
            durationMillis = if (state.secondsLeft != 0L) {
                state.secondsLeft
            } else {
                60_000
            },
            periodMillis = 1000,
        ).launchCollect { timeLeftMillis ->
            state = state.copy(
                secondsLeft = timeLeftMillis / 1000,
            )
        }
    }

    private suspend fun navigatePreviousStepOrBack() {
        if (state.step == LoginState.Step.Code) {
            verifyCodeJob?.cancelAndIfNotFinished {
                countdownJob?.cancel()
                perform(ScrollPagerEffect(LoginState.Step.Phone.number))
                state = state.copy(
                    step = LoginState.Step.Phone,
                    code = "",
                    isCodeValid = true,
                    authState = LoginState.AuthState.Idle,
                    secondsLeft = 0,
                )
            }
        } else {
            navigateBack()
        }
    }

    private suspend fun trySyncAndNavigateBack() {
        val syncResult = deps.syncRepository.sync()
        if (syncResult.isFailure) {
            state = state.copy(
                authState = LoginState.AuthState.Idle,
            )
        } else {
            deps.syncRepository.enableAutoSync()
            navigateBack()
        }
    }
}