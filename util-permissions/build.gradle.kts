import space.getlike.convention.util.utilModule

plugins {
    alias(libs.plugins.utilModule)
}

utilModule("permissions") {
    common(
        implementations = listOf(
            libs.kotlinx.coroutines.core,
        ),
    )

    android(
        implementations = listOf(
            libs.activity.ktx,
            libs.android.core,
        ),
    )

    ios()
}
