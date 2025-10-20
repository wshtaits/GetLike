package space.getlike.client_base.presentation.routes

import kotlinx.serialization.Serializable
import space.getlike.models.LocalContact
import space.getlike.util_core.Route

@ConsistentCopyVisibility
@Serializable
data class InviteRoute private constructor(
    val content: Content,
) : Route() {

    constructor(contact: LocalContact) : this(Content.Contact(contact))

    constructor(phone: String) : this(Content.Phone(phone))

    @Serializable
    sealed interface Content {

        @Serializable
        data class Contact(
            val value: LocalContact,
        ) : Content

        @Serializable
        data class Phone(
            val value: String,
        ) : Content
    }
}