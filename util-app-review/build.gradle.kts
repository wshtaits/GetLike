import space.getlike.convention.util.utilModule

plugins {
    alias(libs.plugins.utilModule)
}

utilModule("app_review") {
    android(
        implementations = listOf(
            libs.review,
            libs.android.core.ktx,
        ),
    )

    ios()
}
