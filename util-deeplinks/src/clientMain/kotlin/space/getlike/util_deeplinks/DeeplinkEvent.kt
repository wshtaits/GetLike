package space.getlike.util_deeplinks

import io.ktor.http.Url

class DeeplinkEvent internal constructor(
    private val url: Url?,
) {

    internal var isMatched = false

    suspend infix fun <TContent> matches(match: DeeplinkMatch<TContent>) {
        if (url == null) {
            return
        }

        if (isMatched) {
            return
        }

        val parsing = match.deeplink.parse(url)

        if (parsing is Deeplink.Parsing.Success<TContent>) {
            match.block(parsing.content)
            isMatched = true
        }
    }
}