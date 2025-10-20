package space.getlike.util_deeplinks

import io.ktor.http.Parameters
import io.ktor.http.ParametersBuilder
import io.ktor.http.URLBuilder
import io.ktor.http.Url

abstract class Deeplink<TContent>(url: String) {

    private val url = Url(url)

    operator fun invoke(content: TContent): String =
        URLBuilder(url)
            .apply {
                parameters.appendAll(
                    Parameters.build { build(this, content) },
                )
            }
            .buildString()

    operator fun Deeplink<Unit>.invoke(): String =
        invoke(Unit)

    operator fun invoke(block: suspend (TContent) -> Unit): DeeplinkMatch<TContent> =
        DeeplinkMatch(this, block)

    internal fun parse(parsingUrl: Url): Parsing<TContent> =
        if (parsingUrl.host == url.host && parsingUrl.encodedPath == url.encodedPath) {
            parse(parsingUrl.parameters)
        } else {
            Parsing.Failure
        }

    protected open fun build(builder: ParametersBuilder, content: TContent) {
    }

    protected abstract fun parse(parameters: Parameters): Parsing<TContent>

    sealed interface Parsing<out TContent> {

        data class Success<TContent>(
            val content: TContent,
        ) : Parsing<TContent>

        data object Failure : Parsing<Nothing>
    }
}