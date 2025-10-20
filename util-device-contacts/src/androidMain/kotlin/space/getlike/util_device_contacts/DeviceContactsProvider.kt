package space.getlike.util_device_contacts

import android.content.ContentResolver
import android.database.ContentObserver
import android.os.Handler
import android.os.Looper
import android.provider.ContactsContract
import androidx.core.database.getStringOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

actual class DeviceContactsProvider(
    private val contentResolver: ContentResolver,
) {

    actual val deviceContactsFlow: Flow<List<DeviceContact>> =
        callbackFlow {
            val observer = object : ContentObserver(Handler(Looper.getMainLooper())) {
                override fun onChange(selfChange: Boolean) {
                    trySend(Unit)
                }
            }

            contentResolver.registerContentObserver(
                /* uri = */ ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                /* notifyForDescendants = */ true,
                /* observer = */ observer,
            )

            awaitClose {
                contentResolver.unregisterContentObserver(observer)
            }
        }
            .map {
                withContext(Dispatchers.IO) {
                    getContacts()
                }
            }

    actual suspend fun getContacts(): List<DeviceContact> =
        contentResolver
            .query(
                /* uri = */ ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                /* projection = */ arrayOf(
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                    ContactsContract.CommonDataKinds.Phone.PHOTO_URI,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.CommonDataKinds.Phone._ID,
                ),
                /* selection = */ null,
                /* selectionArgs = */ null,
                /* sortOrder = */ null,
            )
            ?.use { cursor ->
                val idIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
                val photoIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)
                val nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                val phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                val phoneIdIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID)
                val map = mutableMapOf<String, Triple<String?, String, MutableList<Pair<Long, String>>>>()
                while (cursor.moveToNext()) {
                    val id = cursor.getString(idIndex)
                    val photoUri = cursor.getStringOrNull(photoIndex)
                    val name = cursor.getString(nameIndex)
                    val phone = cursor.getString(phoneIndex)
                    val phoneId = cursor.getLong(phoneIdIndex)
                    map
                        .getOrPut(key = id, defaultValue = { Triple(photoUri, name, mutableListOf()) })
                        .third
                        .add(phoneId to phone)
                }
                map
            }
            ?.map { (contactId, contactData) ->
                val (photoUri, name, phones) = contactData
                DeviceContact(
                    id = contactId,
                    avatarUri = photoUri,
                    name = name,
                    phones = phones.map { (phoneId, phone) ->
                        DevicePhone(
                            id = phoneId.toString(),
                            value = phone,
                        )
                    }
                )
            }
            ?.toList()
            .orEmpty()
}