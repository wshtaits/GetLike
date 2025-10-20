import space.getlike.convention.util.utilModule

plugins {
    alias(libs.plugins.utilModule)
}

utilModule("authentication") {
    common(
        implementations = listOf(
            libs.kotlinx.coroutines.core,
            libs.ktor.client.core,
        ),
    )

    client()

    android(
        apis = listOf(
            libs.firebase.auth.ktx,
            libs.firebase.installations,
        ),
    )

    ios(
        pods = listOf(
            "FirebaseAuth",
            "FirebaseInstallations",
        ),
    )

    jvm(
        implementations = listOf(
            libs.firebase.admin,
            libs.ktor.server.core,
        ),
    )
}
