import space.getlike.convention.util.utilModule

plugins {
    alias(libs.plugins.utilModule)
}

utilModule("app_shortcuts") {
    android(
        implementations = listOf(
            libs.androidx.annotation,
            libs.android.core.ktx,
        ),
    )

    ios()
}
