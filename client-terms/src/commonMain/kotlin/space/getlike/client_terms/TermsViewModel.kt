package space.getlike.client_terms

import space.getlike.client_base.presentation.routes.TermsRoute
import space.getlike.client_base.presentation.viewmodel.ViewModel

class TermsViewModel(
    bundle: Bundle,
) : ViewModel<TermsState, TermsRoute>(
    bundle = bundle,
    analyticsName = "Terms",
    initialState = TermsState(terms = ""),
) {

    override fun onLaunch() = eventOnLaunch {
        state = state.copy(
            terms = deps.loginRepository.getTerms()
                .getOrNull()
                .orEmpty(),
        )
    }

    fun onBackClick() = event("Back", Button, Click) {
        navigateBack()
    }
}