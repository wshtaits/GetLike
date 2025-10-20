package space.getlike.convention.util

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.cocoapods.CocoapodsExtension

fun Project.utilModule(
    name: String,
    configuration: UtilModuleScope.() -> Unit = {},
) {
    val scope = UtilModuleScope()

    scope.configuration()

    if (scope.includeIosPods) {
        pluginManager.apply("org.jetbrains.kotlin.native.cocoapods")
    }

    extensions.configure<LibraryExtension> {
        namespace = "space.getlike.util_$name"
    }

    extensions.configure<KotlinMultiplatformExtension> {
        if (scope.includeAndroid) {
            androidTarget {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_11)
                }
            }
        }

        val iosTargets = if (scope.includeIos) {
            listOf(
                iosX64(),
                iosArm64(),
                iosSimulatorArm64(),
            ).apply {
                forEach { iosTarget ->
                    iosTarget.compilations["main"].defaultSourceSet.languageSettings {
                        optIn("kotlinx.cinterop.BetaInteropApi")
                        optIn("kotlinx.cinterop.ExperimentalForeignApi")
                    }
                }
            }
        } else {
            emptyList()
        }

        if (scope.includeJvm) {
            jvm()
        }

        sourceSets {
            if (scope.includeClient) {
                val commonMain = getByName("commonMain").apply {
                    dependencies {
                        scope.commonApis.forEach(::api)
                        scope.commonImplementations.forEach(::implementation)
                    }
                }

                val clientMain = maybeCreate("clientMain").apply {
                    dependsOn(commonMain)
                    dependencies {
                        scope.clientApis.forEach(::api)
                        scope.clientImplementations.forEach(::implementation)
                    }
                }

                if (scope.includeAndroid) {
                    maybeCreate("androidMain").apply {
                        dependsOn(clientMain)
                        dependencies {
                            scope.androidApis.forEach(::api)
                            scope.androidImplementations.forEach(::implementation)
                        }
                    }
                }

                if (scope.includeIos) {
                    val iosMain = maybeCreate("iosMain").apply {
                        dependsOn(clientMain)
                        dependencies {
                            scope.iosApis.forEach(::api)
                            scope.iosImplementations.forEach(::implementation)
                        }
                        languageSettings {
                            optIn("kotlinx.cinterop.BetaInteropApi")
                            optIn("kotlinx.cinterop.ExperimentalForeignApi")
                        }
                    }
                    iosTargets.forEach { iosTarget ->
                        iosTarget.compilations["main"].defaultSourceSet.dependsOn(iosMain)
                    }
                }

                if (scope.includeJvm) {
                    maybeCreate("jvmMain").apply {
                        dependsOn(commonMain)
                        dependencies {
                            scope.jvmApis.forEach(::api)
                            scope.jvmImplementations.forEach(::implementation)
                        }
                    }
                }
            } else {
                commonMain.dependencies {
                    scope.commonApis.forEach(::api)
                    scope.commonImplementations.forEach(::implementation)
                }

                if (scope.includeAndroid) {
                    androidMain.dependencies {
                        scope.androidApis.forEach(::api)
                        scope.androidImplementations.forEach(::implementation)
                    }
                }

                if (scope.includeIos) {
                    iosMain {
                        dependencies {
                            scope.iosApis.forEach(::api)
                            scope.iosImplementations.forEach(::implementation)
                        }
                        languageSettings {
                            optIn("kotlinx.cinterop.BetaInteropApi")
                            optIn("kotlinx.cinterop.ExperimentalForeignApi")
                        }
                    }
                }

                if (scope.includeJvm) {
                    jvmMain.dependencies {
                        scope.jvmApis.forEach(::api)
                        scope.jvmImplementations.forEach(::implementation)
                    }
                }
            }
        }

        if (scope.includeIosPods) {
            extensions.configure<CocoapodsExtension> {
                version = "1.0"
                summary = "Util $name"
                homepage = "https://getlike.space"
                ios.deploymentTarget = "16.0"

                podfile = project.file("../iosApp/Podfile")

                framework {
                    baseName = "util_$name"
                    isStatic = true
                }

                for (pod in scope.iosPods) {
                    pod(pod) {
                        extraOpts += listOf("-compiler-option", "-fmodules")
                    }
                }

                for (pod in scope.iosBridgedPods) {
                    pod(pod) {
                        extraOpts += listOf("-compiler-option", "-fmodules")
                    }
                    pod("${pod}Bridge") {
                        source = path(project.file("src/iosMain/swift/${pod}Bridge"))
                        extraOpts += listOf("-compiler-option", "-fmodules")
                    }
                }
            }
        }
    }
}
