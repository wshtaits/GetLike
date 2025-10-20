import space.getlike.convention.util.utilModule

plugins {
    alias(libs.plugins.utilModule)
}

utilModule("connection") {
    common(
        implementations = listOf(
            libs.kotlinx.coroutines.core,
        ),
    )

    android()

    ios()
}
