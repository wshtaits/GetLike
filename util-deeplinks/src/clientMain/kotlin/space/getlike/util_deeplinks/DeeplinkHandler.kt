package space.getlike.util_deeplinks

import io.ktor.http.Url
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

object DeeplinkHandler {

    private val _flow = MutableStateFlow(DeeplinkEvent(null))
    val flow: StateFlow<DeeplinkEvent> = _flow

    fun handle(url: String) =
        _flow.update {
            DeeplinkEvent(
                url = try {
                    Url(url)
                } catch (_: Exception) {
                    null
                }
            )
        }
}