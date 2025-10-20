import space.getlike.convention.util.utilModule

plugins {
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.utilModule)
}

utilModule("settings") {
    common(
        implementations = listOf(
            libs.kotlinx.serialization.json,
        ),
    )

    android(
        implementations = listOf(
            libs.android.core.ktx,
            libs.androidx.security.crypto,
        ),
    )

    ios()
}

kotlin {
    sourceSets {
        targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>().configureEach {
            binaries.all {
                linkerOpts("-framework", "Security")
            }
        }
    }
}
