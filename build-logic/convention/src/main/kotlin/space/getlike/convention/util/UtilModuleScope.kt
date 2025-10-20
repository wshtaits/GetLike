package space.getlike.convention.util

class UtilModuleScope internal constructor() {

    internal var commonApis: List<Any> = emptyList()
    internal var commonImplementations: List<Any> = emptyList()

    internal var includeClient: Boolean = false
    internal var clientApis: List<Any> = emptyList()
    internal var clientImplementations: List<Any> = emptyList()

    internal var includeAndroid: Boolean = false
    internal var androidApis: List<Any> = emptyList()
    internal var androidImplementations: List<Any> = emptyList()

    internal var includeIos: Boolean = false
    internal var iosApis: List<Any> = emptyList()
    internal var iosImplementations: List<Any> = emptyList()
    internal var iosPods: List<String> = emptyList()
    internal var iosBridgedPods: List<String> = emptyList()
    internal val includeIosPods: Boolean
        get() = iosPods.isNotEmpty() || iosBridgedPods.isNotEmpty()

    internal var includeJvm: Boolean = false
    internal var jvmApis: List<Any> = emptyList()
    internal var jvmImplementations: List<Any> = emptyList()

    fun common(
        apis: List<Any> = emptyList(),
        implementations: List<Any> = emptyList(),
    ) {
        commonApis = apis
        commonImplementations = implementations
    }

    fun client(
        apis: List<Any> = emptyList(),
        implementations: List<Any> = emptyList(),
    ) {
        includeClient = true
        clientApis = apis
        clientImplementations = implementations
    }

    fun android(
        apis: List<Any> = emptyList(),
        implementations: List<Any> = emptyList(),
    ) {
        includeAndroid = true
        androidApis = apis
        androidImplementations = implementations
    }

    fun ios(
        apis: List<Any> = emptyList(),
        implementations: List<Any> = emptyList(),
        pods: List<String> = emptyList(),
        bridgedPods: List<String> = emptyList(),
    ) {
        includeIos = true
        iosApis = apis
        iosImplementations = implementations
        iosPods = pods
        iosBridgedPods = bridgedPods
    }

    fun jvm(
        apis: List<Any> = emptyList(),
        implementations: List<Any> = emptyList(),
    ) {
        includeJvm = true
        jvmApis = apis
        jvmImplementations = implementations
    }
}
