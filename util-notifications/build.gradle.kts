import space.getlike.convention.util.utilModule

plugins {
    alias(libs.plugins.utilModule)
}

utilModule("notifications") {
    common(
        implementations = listOf(
            libs.kotlinx.coroutines.core,
        ),
    )

    android(
        implementations = listOf(
            libs.android.core,
            libs.android.core.ktx,
            libs.coil,
        ),
    )

    ios()
}
