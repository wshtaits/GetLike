package space.getlike.util_settings

import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.CoreFoundation.CFDictionaryRef
import platform.CoreFoundation.CFRelease
import platform.CoreFoundation.CFTypeRefVar
import platform.Foundation.CFBridgingRelease
import platform.Foundation.CFBridgingRetain
import platform.Foundation.NSData
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.NSUserDefaults
import platform.Foundation.create
import platform.Foundation.dataUsingEncoding
import platform.Security.*

class SettingsImpl(
    private val name: String,
) : Settings() {

    private val defaults = NSUserDefaults(name)

    override fun clear() {
        defaults.removePersistentDomainForName(name)
        defaults.synchronize()

        useDictionaryRef(
            kSecClass to kSecClassGenericPassword,
        ) { ref ->
            SecItemDelete(ref)
        }
    }

    override fun getPlain(key: String): String? =
        defaults.stringForKey(key)

    override fun setPlain(key: String, value: String) {
        defaults.setObject(value, key)
        defaults.synchronize()
    }

    override fun removePlain(key: String) {
        defaults.removeObjectForKey(key)
        defaults.synchronize()
    }

    override fun getSecure(key: String): String? = memScoped {
        val result = alloc<CFTypeRefVar>()

        val status = useDictionaryRef(
            kSecClass to kSecClassGenericPassword,
            kSecAttrAccount to key,
            kSecReturnData to true,
            kSecMatchLimit to kSecMatchLimitOne,
        ) { ref ->
            SecItemCopyMatching(ref, result.ptr)
        }

        if (status != errSecSuccess) {
            return null
        }

        val cfData = result.value ?: return null
        val obj = CFBridgingRelease(cfData) as? NSData ?: return null
        return NSString.create(obj, NSUTF8StringEncoding)?.toString()
    }

    override fun setSecure(key: String, value: String): Unit =
        useDictionaryRef(
            kSecClass to kSecClassGenericPassword,
            kSecAttrAccount to key,
            kSecValueData to NSString.create(value).dataUsingEncoding(NSUTF8StringEncoding),
        ) { ref ->
            SecItemDelete(ref)
            SecItemAdd(attributes = ref, result = null)
        }

    override fun removeSecure(key: String): Unit =
        useDictionaryRef(
            kSecClass to kSecClassGenericPassword,
            kSecAttrAccount to key,
        ) { ref ->
            SecItemDelete(ref)
        }

    private fun <T> useDictionaryRef(
        vararg items: Pair<Any?, Any?>,
        block: (CFDictionaryRef) -> T,
    ): T {
        @Suppress("UNCHECKED_CAST")
        val ref = CFBridgingRetain(items.toMap()) as CFDictionaryRef
        val result = block(ref)
        CFRelease(ref)
        return result
    }
}
