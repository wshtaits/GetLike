package space.getlike.app.configuration

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.routing.Routing
import kotlinx.rpc.krpc.ktor.server.Krpc
import kotlinx.rpc.krpc.ktor.server.rpc
import kotlinx.rpc.krpc.serialization.json.json
import space.getlike.dependencies.Dependencies
import space.getlike.Urls
import space.getlike.presentation.requests.AddContactRequestImpl
import space.getlike.presentation.requests.EditProfileRequestImpl
import space.getlike.presentation.requests.GetChatRequestImpl
import space.getlike.presentation.requests.GetTermsRequestImpl
import space.getlike.presentation.requests.MarkReadRequestImpl
import space.getlike.presentation.requests.MergeContactsRequestImpl
import space.getlike.presentation.requests.RegisterPushTokenRequestImpl
import space.getlike.presentation.requests.SearchContactRequestImpl
import space.getlike.presentation.requests.SendMessageRequestImpl
import space.getlike.presentation.requests.SyncRequestImpl
import space.getlike.requests.*
import space.getlike.presentation.requests.base.register
import space.getlike.util_core.configuration.Configuration

class RpcConfiguration(
    private val deps: Dependencies,
) : Configuration() {

    override fun Application.plugins() {
        install(Krpc) {
            serialization {
                json()
            }
        }
    }

    override fun Routing.routing() {
        rpc {
            rpcConfig {
                serialization {
                    json()
                }
            }
            with(deps) {
                register<AddContactRequest>(::AddContactRequestImpl)
                register<EditProfileRequest>(::EditProfileRequestImpl)
                register<GetChatRequest>(::GetChatRequestImpl)
                register<MarkReadRequest>(::MarkReadRequestImpl)
                register<MergeContactsRequest>(::MergeContactsRequestImpl)
                register<RegisterPushTokenRequest>(::RegisterPushTokenRequestImpl)
                register<SearchContactRequest>(::SearchContactRequestImpl)
                register<SendMessageRequest>(::SendMessageRequestImpl)
                register<SyncRequest>(::SyncRequestImpl)
            }
        }

        rpc(Urls.Paths.NON_AUTH) {
            rpcConfig {
                serialization {
                    json()
                }
            }
            with(deps) {
                register<GetTermsRequest>(::GetTermsRequestImpl)
            }
        }
    }
}