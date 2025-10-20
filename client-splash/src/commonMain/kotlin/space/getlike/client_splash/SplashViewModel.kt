package space.getlike.client_splash

import space.getlike.client_base.presentation.routes.SplashRoute
import space.getlike.client_base.presentation.viewmodel.ViewModel

class SplashViewModel(
    bundle: Bundle,
) : ViewModel<SplashState, SplashRoute>(
    bundle = bundle,
    analyticsName = "Splash",
    initialState = SplashState,
)