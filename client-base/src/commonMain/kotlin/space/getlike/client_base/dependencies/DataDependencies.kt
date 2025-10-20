package space.getlike.client_base.dependencies

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import space.getlike.client_base.data.database.queries.MessageQueries
import space.getlike.client_base.data.database.queries.ProfileQueries
import space.getlike.models.Chat
import space.getlike.models.LocalContact
import space.getlike.models.Message
import space.getlike.models.Profile
import space.getlike.requests.*
import space.getlike.util_authentication.Authentication
import space.getlike.util_device_contacts.DeviceContactsProvider
import space.getlike.util_messaging.ClientMessagingSender
import space.getlike.util_messaging.PushTokenProvider
import space.getlike.util_settings.Settings

interface DataDependencies : CommonDependencies {

    val authentication: Authentication

    val settings: Settings

    val deviceContactsProvider: DeviceContactsProvider

    val pushTokenProvider: PushTokenProvider

    val messagingSender: ClientMessagingSender

    val coroutineScope: CoroutineScope

    var isAutoSyncEnabled: Boolean

    val isSyncingFlow: MutableStateFlow<Boolean>
    val selfProfileFlow: MutableStateFlow<Profile?>
    val messagesFlow: MutableStateFlow<List<Message>>
    val contactsFlow: MutableStateFlow<List<Profile>>
    val localContactsFlow: MutableStateFlow<List<LocalContact>>
    val registeredLocalContactIdsFlow: MutableStateFlow<List<String>>

    val addContactRequest: AddContactRequest
    val editProfileRequest: EditProfileRequest
    val getChatRequest: GetChatRequest
    val getTermsRequest: GetTermsRequest
    val markReadRequest: MarkReadRequest
    val mergeContactsRequest: MergeContactsRequest
    val registerPushTokenRequest: RegisterPushTokenRequest
    val searchContactRequest: SearchContactRequest
    val sendMessageRequest: SendMessageRequest
    val syncRequest: SyncRequest

    val messageQueries: MessageQueries
    val profileQueries: ProfileQueries

    fun configureKtor()
}