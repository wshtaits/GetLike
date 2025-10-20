import space.getlike.convention.util.utilModule

plugins {
    alias(libs.plugins.utilModule)
}

utilModule("logger") {
    android(
        apis = listOf(
            libs.firebase.analytics,
            libs.firebase.crashlytics,
        ),
    )

    ios(
        pods = listOf(
            "FirebaseAnalytics",
            "FirebaseCrashlytics",
        ),
    )

    jvm()
}
