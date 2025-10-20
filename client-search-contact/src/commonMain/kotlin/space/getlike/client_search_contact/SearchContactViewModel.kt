package space.getlike.client_search_contact

import kotlinx.coroutines.Job
import space.getlike.client_base.presentation.broadcasts.ShowProfileBroadcast
import space.getlike.client_base.presentation.effects.HideKeyboardEffect
import space.getlike.client_base.presentation.viewmodel.ViewModel
import space.getlike.client_base.presentation.routes.ContactRoute
import space.getlike.client_base.presentation.routes.InviteRoute
import space.getlike.client_base.presentation.routes.SearchContactRoute
import space.getlike.requests.SearchContactRequest
import space.getlike.util_core.utils.cancelAndIfNotFinished
import space.getlike.util_core.utils.isNullOrCompleted

class SearchContactViewModel(
    bundle: Bundle,
) : ViewModel<SearchContactState, SearchContactRoute>(
    bundle = bundle,
    analyticsName = "SearchContact",
    initialState = { deps, _ ->
        SearchContactState(
            phone = "",
            phoneState = SearchContactState.PhoneState.Invalid,
            hasConnection = deps.connection.isConnectedFlow.value,
        )
    },
) {

    private var searchContactJob: Job? = null

    init {
        deps.connection.isConnectedFlow.launchCollect { isConnected ->
            state = state.copy(
                hasConnection = isConnected,
            )
        }
    }

    override fun onSystemBackClick() = eventOnSystemBack {
        searchContactJob?.cancel()
        navigateBack()
    }

    fun onBackClick() = event("Back", Button, Click) {
        searchContactJob?.cancel()
        navigateBack()
    }

    fun onPhoneClick() = event("Phone", Field, Click) {
        // no op
    }

    fun onPhoneChange(phone: String) = event("Phone", Field, Change) {
        state = state.copy(
            phone = phone,
            phoneState = if (deps.phoneFormatter.isValid(phone)) {
                SearchContactState.PhoneState.Valid
            } else {
                SearchContactState.PhoneState.Invalid
            },
        )
    }

    fun onPhoneDoneClick() = event("PhoneDone", KeyboardButton, Click) {
        perform(HideKeyboardEffect)
        if (state.phoneState == SearchContactState.PhoneState.Valid) {
            searchContact()
        }
    }

    fun onActionClick() = event("Action", Button, Click, "state" to state.phoneState) {
        if (searchContactJob.isNullOrCompleted) {
            perform(HideKeyboardEffect)
            when (state.phoneState) {
                SearchContactState.PhoneState.Valid ->
                    searchContact()
                SearchContactState.PhoneState.NotRegistered -> {
                    val phone = deps.phoneFormatter.format(state.phone)
                    navigateModal(InviteRoute(phone))
                }
                else -> { /* no op */ }
            }
        } else {
            searchContactJob?.cancelAndIfNotFinished {
                state = state.copy(
                    phoneState = SearchContactState.PhoneState.Valid,
                )
            }
        }
    }

    private fun searchContact() {
        searchContactJob = launch {
            state = state.copy(phoneState = SearchContactState.PhoneState.Search)
            val result = deps.profileRepository.searchContact(state.phone).getOrNull()
            when (result) {
                null ->
                    state = state.copy(phoneState = SearchContactState.PhoneState.Valid)
                is SearchContactRequest.Result.NotRegistered ->
                    state = state.copy(phoneState = SearchContactState.PhoneState.NotRegistered)
                is SearchContactRequest.Result.Self -> {
                    navigateBack()
                    deps.broadcast.send(ShowProfileBroadcast)
                }
                is SearchContactRequest.Result.Success -> {
                    navigateRoot(
                        route = ContactRoute(result.contactId),
                        replace = true,
                    )
                }
            }
        }
    }
}