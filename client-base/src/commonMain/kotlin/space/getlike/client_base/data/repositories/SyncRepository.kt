package space.getlike.client_base.data.repositories

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import space.getlike.Emojis
import space.getlike.client_base.dependencies.DataDependencies
import space.getlike.models.Avatar
import space.getlike.models.LocalContact
import space.getlike.util_core.Repository
import space.getlike.util_device_contacts.DeviceContact
import space.getlike.util_permissions.Permission
import kotlin.collections.filter

class SyncRepository(
    private val deps: DataDependencies,
) : Repository(deps.coroutineScope) {

    val syncState: SyncState
        get() = when {
            deps.isSyncingFlow.value -> SyncState.Loading
            deps.connection.isConnectedFlow.value -> SyncState.Connected
            else -> SyncState.Disconnected
        }

    val syncStateFlow: StateFlow<SyncState> =
        combine(
            deps.isSyncingFlow,
            deps.connection.isConnectedFlow,
            transform = { syncState },
        )
            .distinctUntilChanged()
            .stateIn(syncState)

    private val isConnectedAndAuthedFlow: Flow<Boolean> =
        combine(
            deps.connection.isConnectedFlow,
            deps.authentication.isAuthedFlow,
            transform = { isConnected, isAuthed -> isConnected && isAuthed },
        )
            .drop(1)
            .filter { isConnectedAndAuthed -> isConnectedAndAuthed }

    private val initDeferred = CompletableDeferred<Unit>()
    private val initMutex = Mutex()

    init {
        launch {
            isConnectedAndAuthedFlow
                .collect {
                    try {
                        if (deps.isAutoSyncEnabled) {
                            deps.isSyncingFlow.update { true }
                            deps.configureKtor()
                            sync()
                        } else {
                            deps.configureKtor()
                        }
                    } finally {
                        deps.isSyncingFlow.update { false }
                    }
                }
        }

        if (deps.permissionManager.has(Permission.Contacts)) {
            launchCollectDeviceContacts()
        }

        deps.permissionManager.observe(Permission.Contacts).launchCollect { hasPermission ->
            if (hasPermission) {
                launchCollectDeviceContacts()
            }
        }
    }

    suspend fun awaitInit() {
        initDeferred.await()
    }

    suspend fun init() = executeIo {
        initMutex.withLock {
            val profileId = deps.authentication.id ?: return@executeIo
            val profiles = deps.profileQueries.selectAll()
            val profile = profiles.find { profile -> profile.id == profileId } ?: return@executeIo
            val contacts = profiles.filter { contact -> contact.id != profileId }
            val messages = deps.messageQueries.selectAll()
            val localContacts = getLocalContacts()
            val registeredLocalContactIds = deps.profileQueries.selectAllRegisteredLocalContactIds()

            deps.selfProfileFlow.update { profile }
            deps.contactsFlow.update { contacts }
            deps.messagesFlow.update { messages }
            deps.localContactsFlow.update { localContacts }
            deps.registeredLocalContactIdsFlow.update { registeredLocalContactIds }

            initDeferred.complete(Unit)
        }
    }

    suspend fun sync() = executeIoCatching(
        onTry = {
            deps.isSyncingFlow.update { true }

            val (profile, contacts, messages, registeredLocalContactIds) = deps.syncRequest.execute(
                pushToken = deps.pushTokenProvider.get(),
                markedReadContactIds = deps.messageQueries.selectAllDeferredMarkedRead(),
                unsentMessages = deps.messagesFlow.value.filter { message -> message.isUnsent },
                localContacts = deps.localContactsFlow.value
                    .filter { localContact -> localContact.id !in deps.registeredLocalContactIdsFlow.value },
            )

            deps.selfProfileFlow.update { profile }
            deps.contactsFlow.update { contacts }
            deps.messagesFlow.update { messages }
            deps.registeredLocalContactIdsFlow.update { registeredLocalContactIds }

            deps.messageQueries.deleteAllDeferredMarkedRead()

            deps.profileQueries.replaceAll(contacts + profile)
            deps.messageQueries.replaceAll(messages)
            deps.profileQueries.upsertRegisteredLocalContactIds(registeredLocalContactIds)
        },
        onFinally = {
            deps.isSyncingFlow.update { false }
        },
    )

    fun enableAutoSync() {
        deps.isAutoSyncEnabled = true
    }

    private fun launchCollectDeviceContacts() = launch {
        deps.deviceContactsProvider.deviceContactsFlow
            .filter { deps.isAutoSyncEnabled }
            .stateIn(deps.deviceContactsProvider.getContacts())
            .launchCollect { deviceContacts ->
                val localContacts = deviceContacts.map { deviceContact -> deviceContact.toLocalContact() }
                mergeLocalContacts(localContacts)
            }
    }

    private suspend fun mergeLocalContacts(localContacts: List<LocalContact>) {
        val (profile, contacts, registeredLocalContactIds) = deps.mergeContactsRequest.execute(localContacts)
        deps.selfProfileFlow.update { profile }
        deps.contactsFlow.update { contacts }
        deps.localContactsFlow.update { localContacts }
        deps.registeredLocalContactIdsFlow.update { registeredLocalContactIds }
    }

    private suspend fun getLocalContacts(): List<LocalContact> {
        val deviceContacts = if (deps.permissionManager.has(Permission.Contacts)) {
            deps.deviceContactsProvider.getContacts()
        } else {
            emptyList()
        }
        return deviceContacts.map { deviceContact -> deviceContact.toLocalContact() }
    }

    private fun DeviceContact.toLocalContact() =
        LocalContact(
            id = id,
            name = name,
            avatar = Avatar(
                uri = avatarUri,
                fallbackEmoji = Emojis.fromString(name),
            ),
            phones = phones
                .map { phone ->
                    try {
                        deps.phoneFormatter.format(phone.value)
                    } catch (_: Throwable) {
                        phone.value
                    }
                }
                .distinct(),
        )

    enum class SyncState {
        Loading,
        Connected,
        Disconnected,
    }
}
