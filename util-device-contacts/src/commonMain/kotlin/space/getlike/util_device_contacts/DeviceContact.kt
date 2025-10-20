package space.getlike.util_device_contacts

data class DeviceContact(
    val id: String,
    val name: String,
    val avatarUri: String?,
    val phones: List<DevicePhone>,
)