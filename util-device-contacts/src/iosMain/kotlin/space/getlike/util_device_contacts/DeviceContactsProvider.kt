package space.getlike.util_device_contacts

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import platform.Contacts.*
import platform.Foundation.NSDataBase64Encoding64CharacterLineLength
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.Foundation.base64EncodedStringWithOptions

actual class DeviceContactsProvider(
    private val coroutineScope: CoroutineScope,
) {

    private val contactStore = CNContactStore()

    actual val deviceContactsFlow: Flow<List<DeviceContact>> = callbackFlow {
        val observer = NSNotificationCenter.defaultCenter.addObserverForName(
            name = CNContactStoreDidChangeNotification,
            `object` = null,
            queue = NSOperationQueue.mainQueue,
            usingBlock = { coroutineScope.launch { trySend(getContacts()) } },
        )
        awaitClose { NSNotificationCenter.defaultCenter.removeObserver(observer) }
    }

    actual suspend fun getContacts(): List<DeviceContact> {
        val contacts = mutableListOf<DeviceContact>()

        val request = CNContactFetchRequest(
            keysToFetch = listOf(
                CNContactIdentifierKey,
                CNContactGivenNameKey,
                CNContactFamilyNameKey,
                CNContactPhoneNumbersKey,
                CNContactThumbnailImageDataKey,
            ),
        )

        try {
            contactStore.enumerateContactsWithFetchRequest(
                fetchRequest = request,
                error = null,
            ) { contact, stop ->
                contact ?: return@enumerateContactsWithFetchRequest

                contacts.add(
                    DeviceContact(
                        id = contact.identifier,
                        name = "${contact.givenName} ${contact.familyName}".trim(),
                        avatarUri = contact.thumbnailImageData
                            ?.base64EncodedStringWithOptions(NSDataBase64Encoding64CharacterLineLength)
                            ?.let { base64 -> "data:image/png;base64,$base64" },
                        phones = contact.phoneNumbers
                            .mapNotNull { labeledValueAny ->
                                val labeledValue = labeledValueAny as? CNLabeledValue ?: return@mapNotNull null
                                val phoneNumber = labeledValue.value as? CNPhoneNumber
                                    ?: return@mapNotNull null
                                DevicePhone(
                                    id = labeledValue.identifier,
                                    value = phoneNumber.stringValue,
                                )
                            },
                    )
                )
            }
        } catch (_: Throwable) {
            // no op
        }

        return contacts
    }
}
