package space.getlike.util_device_contacts

import kotlinx.coroutines.flow.Flow

expect class DeviceContactsProvider {

    val deviceContactsFlow: Flow<List<DeviceContact>>

    suspend fun getContacts(): List<DeviceContact>
}