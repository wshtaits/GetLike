package space.getlike.client_base.dependencies

import androidx.lifecycle.SavedStateHandle
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.rpc.RpcClient
import kotlinx.rpc.krpc.ktor.client.installKrpc
import kotlinx.rpc.krpc.ktor.client.rpc
import kotlinx.rpc.krpc.ktor.client.rpcConfig
import kotlinx.rpc.krpc.serialization.json.json
import kotlinx.rpc.withService
import kotlinx.serialization.json.Json
import space.getlike.Urls
import space.getlike.client_base.data.database.queries.MessageQueries
import space.getlike.client_base.data.database.queries.ProfileQueries
import space.getlike.client_base.data.repositories.ChatRepository
import space.getlike.client_base.data.repositories.LoginRepository
import space.getlike.client_base.data.repositories.ProfileRepository
import space.getlike.client_base.data.repositories.SyncRepository
import space.getlike.util_core.Broadcast
import space.getlike.client_base.presentation.messaging.AddContactHandler
import space.getlike.client_base.presentation.messaging.ProfileChangedHandler
import space.getlike.client_base.presentation.messaging.NewAchievementHandler
import space.getlike.client_base.presentation.messaging.ReadMessagesHandler
import space.getlike.client_base.presentation.messaging.SendMessageHandler
import space.getlike.models.LocalContact
import space.getlike.models.Message
import space.getlike.models.Profile
import space.getlike.requests.*
import space.getlike.util_authentication.ClientAuthenticationPlugin
import space.getlike.util_core.utils.saveableStateFlow
import space.getlike.util_core.utils.saveable
import space.getlike.util_messaging.ClientMessagingSender
import space.getlike.util_messaging.webSocket
import space.getlike.util_phone_number.PhoneFormatter

class Dependencies(
    factory: PlatformDependenciesFactory,
    savedStateHandle: SavedStateHandle,
    override val coroutineScope: CoroutineScope,
) : DataDependencies, PresentationDependencies {

    //region CommonDependencies

    override val connection by lazy { factory.connection(coroutineScope) }

    override val permissionManager by lazy { factory.permissionManager() }

    override val phoneFormatter by lazy { PhoneFormatter() }

    //endregion

    //region DataDependencies

    override val authentication by lazy { factory.authentication() }

    override val settings by lazy { factory.settings(Constants.Settings.NAME) }

    override val deviceContactsProvider by lazy { factory.deviceContactsProvider(coroutineScope) }

    override val pushTokenProvider by lazy { factory.pushTokenProvider() }

    override val messagingSender by lazy { ClientMessagingSender() }

    override var isAutoSyncEnabled by savedStateHandle.saveable(false)

    override val isSyncingFlow by savedStateHandle.saveableStateFlow(false)
    override val selfProfileFlow by savedStateHandle.saveableStateFlow(null as Profile?)
    override val messagesFlow by savedStateHandle.saveableStateFlow(emptyList<Message>())
    override val contactsFlow by savedStateHandle.saveableStateFlow(emptyList<Profile>())
    override val localContactsFlow by savedStateHandle.saveableStateFlow(emptyList<LocalContact>())
    override val registeredLocalContactIdsFlow by savedStateHandle.saveableStateFlow(emptyList<String>())

    override val addContactRequest
        get() = rpcClient.withService<AddContactRequest>()
    override val editProfileRequest
        get() = rpcClient.withService<EditProfileRequest>()
    override val getChatRequest
        get() = rpcClient.withService<GetChatRequest>()
    override val getTermsRequest
        get() = nonAuthRpcClient.withService<GetTermsRequest>()
    override val markReadRequest
        get() = rpcClient.withService<MarkReadRequest>()
    override val mergeContactsRequest
        get() = rpcClient.withService<MergeContactsRequest>()
    override val registerPushTokenRequest
        get() = rpcClient.withService<RegisterPushTokenRequest>()
    override val searchContactRequest
        get() = rpcClient.withService<SearchContactRequest>()
    override val sendMessageRequest
        get() = rpcClient.withService<SendMessageRequest>()
    override val syncRequest
        get() = rpcClient.withService<SyncRequest>()

    override val messageQueries by lazy { MessageQueries(database) }
    override val profileQueries by lazy { ProfileQueries(database) }

    //endregion

    //region PresentationDependencies

    override val logger by lazy { factory.logger() }

    override val share by lazy { factory.share() }

    override val regionProvider by lazy { factory.regionProvider() }

    override val notificationDisplayer by lazy { factory.notificationDisplayer() }

    override val imagePicker by lazy { factory.imagePicker() }

    override val appLifecycle by lazy { factory.appLifecycle() }

    override val appReview by lazy { factory.appReview() }

    override val appShortcuts by lazy { factory.appShortcuts() }

    override val appShortcutItems by lazy { factory.appShortcutItems() }

    override val broadcast by lazy { Broadcast() }

    override val chatRepository by lazy { ChatRepository(this) }
    override val syncRepository by lazy { SyncRepository(this) }
    override val loginRepository by lazy { LoginRepository(this) }
    override val profileRepository by lazy { ProfileRepository(this) }

    //endregion

    val messageHandlers by lazy {
        listOf(
            AddContactHandler(this),
            ProfileChangedHandler(this),
            NewAchievementHandler(this),
            ReadMessagesHandler(this),
            SendMessageHandler(this),
        )
    }

    private val database by lazy { factory.database(Constants.Database.NAME) }
    private val platform by lazy { factory.platform() }

    private lateinit var rpcClient: RpcClient
    private lateinit var nonAuthRpcClient: RpcClient

    init {
        configureKtor()
        configureLogger()
    }

    override fun configureKtor() {
        val httpClient = HttpClient {
            install(WebSockets) {
                contentConverter = KotlinxWebsocketSerializationConverter(Json)
            }
            install(ClientAuthenticationPlugin) {
                authentication = this@Dependencies.authentication
            }
            installKrpc()
        }

        rpcClient = httpClient.rpc {
            url {
                protocol = Urls.Segments.Client.PROTOCOL
                host = Urls.Segments.Client.HOST
                port = Urls.Segments.Client.PORT
            }
            rpcConfig {
                serialization {
                    json()
                }
            }
        }

        nonAuthRpcClient = httpClient.rpc {
            url {
                protocol = Urls.Segments.Client.PROTOCOL
                host = Urls.Segments.Client.HOST
                port = Urls.Segments.Client.PORT
                encodedPath = Urls.Paths.NON_AUTH
            }
            rpcConfig {
                serialization {
                    json()
                }
            }
        }

        coroutineScope.launch {
            httpClient.webSocket(
                url = Urls.WEB_SOCKET,
                sender = messagingSender,
                handlers = messageHandlers,
            )
        }
    }

    private fun configureLogger() {
        logger.setCustomKey(Constants.Logger.CUSTOM_KEY_PLATFORM, platform)
    }
}