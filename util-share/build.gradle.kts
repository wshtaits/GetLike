import space.getlike.convention.util.utilModule

plugins {
    alias(libs.plugins.utilModule)
}

utilModule("share") {
    android(
        implementations = listOf(
            libs.android.core.ktx,
        ),
    )

    ios()
}
