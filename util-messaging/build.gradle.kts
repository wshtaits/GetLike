import space.getlike.convention.util.utilModule

plugins {
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.utilModule)
}

utilModule("messaging") {
    common(
        implementations = listOf(
            libs.kotlinx.coroutines.core,
            libs.kotlinx.serialization.json,
        )
    )

    client(
        apis = listOf(
            libs.kotlinx.rpc.krpc.client,
            libs.ktor.client.content.negotiation,
            libs.ktor.client.core,
            libs.ktor.client.websockets,
        )
    )

    android(
        apis = listOf(
            libs.firebase.messaging,
        ),
    )

    ios(
        pods = listOf(
            "FirebaseMessaging",
        ),
    )

    jvm(
        implementations = listOf(
            libs.firebase.admin,
            libs.ktor.server.content.negotiation,
            libs.ktor.server.core,
            libs.ktor.server.netty,
            libs.ktor.server.websockets,
        ),
    )
}
