package space.getlike.util_share

internal interface Sharing {

    fun isAvailable(): Boolean

    fun text(text: String, title: String?, phone: String?)
}