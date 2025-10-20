package space.getlike.util_permissions.handlers

import platform.Contacts.CNAuthorizationStatusAuthorized
import platform.Contacts.CNContactStore
import platform.Contacts.CNEntityType.CNEntityTypeContacts
import space.getlike.util_permissions.PermissionHandler

class ContactsPermissionHandler : PermissionHandler {

    override fun has(): Boolean =
        CNContactStore.authorizationStatusForEntityType(CNEntityTypeContacts) == CNAuthorizationStatusAuthorized

    override fun request(onResult: (Boolean) -> Unit) =
        CNContactStore().requestAccessForEntityType(CNEntityTypeContacts) { granted, _ -> onResult(granted) }
}
