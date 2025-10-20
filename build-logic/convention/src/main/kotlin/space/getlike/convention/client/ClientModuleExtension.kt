package space.getlike.convention.client

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

fun Project.clientModule(
    name: String,
    configuration: ClientModuleScope.() -> Unit = {},
) {
    val scope = ClientModuleScope()

    scope.configuration()

    extensions.configure<LibraryExtension> {
        namespace = "space.getlike.client_$name"
    }

    extensions.configure<KotlinMultiplatformExtension> {
        sourceSets {
            commonMain.dependencies {
                scope.commonApis.forEach(::api)
                scope.commonImplementations.forEach(::implementation)
            }

            androidMain.dependencies {
                scope.androidApis.forEach(::api)
                scope.androidImplementations.forEach(::implementation)
            }

            iosMain.dependencies {
                scope.iosApis.forEach(::api)
                scope.iosImplementations.forEach(::implementation)
            }
        }
    }
}
