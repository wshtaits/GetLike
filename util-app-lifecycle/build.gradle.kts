import space.getlike.convention.util.utilModule

plugins {
    alias(libs.plugins.utilModule)
}

utilModule("app_lifecycle") {
    android()

    ios()
}
