import space.getlike.convention.util.utilModule

plugins {
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.utilModule)
}

utilModule("phone_number") {
    common(
        apis = listOf(
            libs.compose.ui,
        ),
    )

    android(
        implementations = listOf(
            libs.libphonenumber,
        ),
    )

    ios(
        bridgedPods = listOf(
            "PhoneNumberKit",
        ),
    )
}
