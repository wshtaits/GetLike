package space.getlike.deeplinks

import io.ktor.http.Parameters
import io.ktor.http.ParametersBuilder
import space.getlike.util_deeplinks.Deeplink

data object ChatDeeplink : Deeplink<String>("https://getlike.space/chat") {

    override fun build(builder: ParametersBuilder, content: String) {
        builder.append("profile_id", content)
    }

    override fun parse(parameters: Parameters): Parsing<String> {
        val profileId = parameters["profile_id"]
        return if (!profileId.isNullOrEmpty()) {
            Parsing.Success(profileId)
        } else {
            Parsing.Failure
        }
    }
}