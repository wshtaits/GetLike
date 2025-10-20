import space.getlike.convention.util.utilModule

plugins {
    alias(libs.plugins.utilModule)
}

utilModule("image_picker") {
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
