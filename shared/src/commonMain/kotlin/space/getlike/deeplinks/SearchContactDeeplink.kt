package space.getlike.deeplinks

import io.ktor.http.Parameters
import space.getlike.util_deeplinks.Deeplink

data object SearchContactDeeplink : Deeplink<Unit>("https://getlike.space/search") {

    override fun parse(parameters: Parameters): Parsing<Unit> =
        Parsing.Success(Unit)
}