import space.getlike.convention.util.utilModule

plugins {
    alias(libs.plugins.utilModule)
}

utilModule("deeplinks") {
    common(
        implementations = listOf(
            libs.kotlinx.coroutines.core,
            libs.ktor.client.core,
        ),
    )

    client()

    android()

    ios()

    jvm()
}
