rootProject.name = "GetLike"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

include(":androidApp")
include(":client-base")
include(":client-chat")
include(":client-chats")
include(":client-contact")
include(":client-contacts")
include(":client-login")
include(":client-main")
include(":client-search-contact")
include(":client-profile")
include(":client-profile-editor")
include(":client-invite")
include(":client-splash")
include(":client-terms")
include(":server")
include(":shared")
include(":util-app-lifecycle")
include(":util-app-review")
include(":util-app-shortcuts")
include(":util-authentication")
include(":util-connection")
include(":util-core")
include(":util-deeplinks")
include(":util-device-contacts")
include(":util-image-picker")
include(":util-logger")
include(":util-messaging")
include(":util-notifications")
include(":util-permissions")
include(":util-phone-number")
include(":util-region")
include(":util-settings")
include(":util-share")
