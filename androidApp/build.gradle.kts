import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.firebaseCrashlytics)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "AndroidApp"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.clientBase)
            implementation(projects.clientChat)
            implementation(projects.clientContact)
            implementation(projects.clientInvite)
            implementation(projects.clientLogin)
            implementation(projects.clientMain)
            implementation(projects.clientSearchContact)
            implementation(projects.clientProfileEditor)
            implementation(projects.clientSplash)
            implementation(projects.clientTerms)
        }
        androidMain.dependencies {
            implementation(libs.firebase.core)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.androidx.core.splashscreen)
        }
    }
}

android {
    namespace = "space.getlike"
    compileSdk = libs.versions.androidCompileSdk.get().toInt()

    defaultConfig {
        applicationId = "space.getlike"
        minSdk = libs.versions.androidMinSdk.get().toInt()
        targetSdk = libs.versions.androidTargetSdk.get().toInt()
        versionCode = libs.versions.androidAppVersionCode.get().toInt()
        versionName = libs.versions.androidAppVersionName.get()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}