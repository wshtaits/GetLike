package space.getlike.client_terms

import kotlinx.serialization.Serializable

@Serializable
data class TermsState(
    val terms: String,
)