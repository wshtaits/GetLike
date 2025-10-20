package space.getlike.util_settings

import kotlinx.serialization.serializer

class SettingsAccessor internal constructor(
    @PublishedApi internal val type: SettingsType,
    @PublishedApi internal val settings: Settings,
) {

    inline operator fun <reified T : Any> get(key: String): T? =
        settings.get(key, type, serializer())

    inline operator fun <reified T : Any> set(key: String, value: T) =
        settings.set(key, type, serializer(), value)

    fun remove(key: String) =
        settings.remove(key, type)
}