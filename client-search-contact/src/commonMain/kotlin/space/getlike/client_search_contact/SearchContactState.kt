package space.getlike.client_search_contact

import kotlinx.serialization.Serializable

@Serializable
data class SearchContactState(
    val phone: String,
    val phoneState: PhoneState,
    val hasConnection: Boolean,
) {

    enum class PhoneState {
        Invalid,
        Valid,
        Search,
        NotRegistered,
    }
}