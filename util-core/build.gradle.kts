import space.getlike.convention.util.utilModule

plugins {
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.utilModule)
}

utilModule("core") {
    common(
        apis = listOf(
            libs.compose.runtime,
            libs.kotlinx.coroutines.core,
            libs.kotlinx.datetime,
            libs.kotlinx.rpc.krpc.serialization.json,
            libs.kotlinx.serialization.json,
            libs.ktor.serialization.kotlinx.json,
        ),
    )

    client(
        apis = listOf(
            libs.androidx.lifecycle.runtime.compose,
            libs.androidx.lifecycle.viewmodel,
            libs.androidx.navigation3.runtime,
            libs.androidx.navigation3.ui,
            libs.compose.foundation,
            libs.compose.material3,
        ),
    )

    android(
        apis = listOf(
            libs.androidx.activity.compose,
        ),
    )

    ios()

    jvm(
        apis = listOf(
            libs.kotlinx.rpc.krpc.ktor.server,
            libs.ktor.server.core,
        ),
    )
}
