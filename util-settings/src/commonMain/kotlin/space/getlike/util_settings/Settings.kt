package space.getlike.util_settings

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

abstract class Settings internal constructor() {

    val plain by lazy { SettingsAccessor(SettingsType.Plain, this) }
    val secure by lazy { SettingsAccessor(SettingsType.Secure, this) }

    abstract fun clear()

    @PublishedApi
    internal fun <T : Any?> get(key: String, type: SettingsType, serializer: KSerializer<T>): T? {
        val json = when (type) {
            SettingsType.Plain -> getPlain(key)
            SettingsType.Secure -> getSecure(key)
        }
        return if (json?.isNotEmpty() == true) {
            Json.decodeFromString(serializer, json)
        } else {
            null
        }
    }

    @PublishedApi
    internal fun <T : Any> set(key: String, type: SettingsType, serializer: KSerializer<T>, value: T) {
        val json = Json.encodeToString(serializer, value)
        when (type) {
            SettingsType.Plain -> setPlain(key, json)
            SettingsType.Secure -> setSecure(key, json)
        }
    }

    internal fun remove(key: String, type: SettingsType) =
        when (type) {
            SettingsType.Plain -> removePlain(key)
            SettingsType.Secure -> removeSecure(key)
        }

    protected abstract fun getPlain(key: String): String?

    protected abstract fun setPlain(key: String, value: String)

    protected abstract fun removePlain(key: String)

    protected abstract fun getSecure(key: String): String?

    protected abstract fun setSecure(key: String, value: String)

    protected abstract fun removeSecure(key: String)
}