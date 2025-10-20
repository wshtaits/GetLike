package space.getlike.convention.client

class ClientModuleScope internal constructor() {

    internal var commonApis: List<Any> = emptyList()
    internal var commonImplementations: List<Any> = emptyList()

    internal var androidApis: List<Any> = emptyList()
    internal var androidImplementations: List<Any> = emptyList()

    internal var iosApis: List<Any> = emptyList()
    internal var iosImplementations: List<Any> = emptyList()

    fun common(
        apis: List<Any> = emptyList(),
        implementations: List<Any> = emptyList(),
    ) {
        commonApis = apis
        commonImplementations = implementations
    }

    fun android(
        apis: List<Any> = emptyList(),
        implementations: List<Any> = emptyList(),
    ) {
        androidApis = apis
        androidImplementations = implementations
    }

    fun ios(
        apis: List<Any> = emptyList(),
        implementations: List<Any> = emptyList(),
    ) {
        iosApis = apis
        iosImplementations = implementations
    }
}
