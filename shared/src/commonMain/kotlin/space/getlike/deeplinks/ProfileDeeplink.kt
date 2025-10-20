package space.getlike.deeplinks

import io.ktor.http.Parameters
import io.ktor.http.ParametersBuilder
import space.getlike.util_deeplinks.Deeplink

data object ProfileDeeplink : Deeplink<String>("https://getlike.space/profile") {

    override fun build(builder: ParametersBuilder, content: String) {
        builder.append("id", content)
    }

    override fun parse(parameters: Parameters): Parsing<String> {
        val profileId = parameters["id"]
        return if (!profileId.isNullOrEmpty()) {
            Parsing.Success(profileId)
        } else {
            Parsing.Failure
        }
    }
}