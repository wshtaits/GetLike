package space.getlike.util_deeplinks

class DeeplinkMatch<TContent>(
    val deeplink: Deeplink<TContent>,
    val block: suspend (TContent) -> Unit,
)