import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinRpc)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
        freeCompilerArgs.add("-Xexpect-actual-classes")
        freeCompilerArgs.add("-opt-in=kotlin.time.ExperimentalTime")
    }

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).apply {
        forEach { iosTarget ->
            iosTarget.compilations["main"].defaultSourceSet.languageSettings {
                optIn("kotlinx.cinterop.ExperimentalForeignApi")
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.androidx.constraint.layout)
            api(libs.coil.compose)
            api(libs.compose.componentsResources)
            api(libs.compose.ui)
            api(libs.kotlinx.rpc.krpc.ktor.client)
            api(libs.ktor.client.content.negotiation)
            api(libs.ktor.client.core)
            api(libs.ktor.client.websockets)
            api(libs.multiplatform.markdown.renderer)
            api(projects.shared)
            api(projects.utilAppLifecycle)
            api(projects.utilAppReview)
            api(projects.utilAppShortcuts)
            api(projects.utilConnection)
            api(projects.utilDeviceContacts)
            api(projects.utilImagePicker)
            api(projects.utilNotifications)
            api(projects.utilPermissions)
            api(projects.utilPhoneNumber)
            api(projects.utilRegion)
            api(projects.utilSettings)
            api(projects.utilShare)
            implementation(libs.androidx.room.runtime)
        }
        androidMain.dependencies {
            api(libs.coil.network.okhttp)
            api(libs.compose.ui.tooling)
            api(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.work.runtime.ktx)
            implementation(libs.firebase.messaging)
            implementation(libs.lazy.column.scrollbar)
        }
        iosMain {
            dependencies {
                api(libs.ktor.client.darwin)
            }
            languageSettings {
                optIn("kotlinx.cinterop.ExperimentalForeignApi")
            }
        }
    }
}

android {
    namespace = "space.getlike.client_base"
    compileSdk = libs.versions.androidCompileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.androidMinSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugApi(compose.uiTooling)
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    add("kspIosX64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
}

compose.resources {
    publicResClass = true
    packageOfResClass = "space.getlike.resources"
    generateResClass = auto
}

room {
    schemaDirectory("$projectDir/schemas")
}
