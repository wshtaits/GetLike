plugins {
    `kotlin-dsl`
}

group = "space.getlike.convention"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    compileOnly(libs.gradle.androidTools)
    compileOnly(libs.gradle.kotlin)
}

gradlePlugin {
    plugins {
        register("clientModule") {
            id = "space.getlike.convention.client-module"
            implementationClass = "space.getlike.convention.client.ClientModulePlugin"
        }
        register("utilModule") {
            id = "space.getlike.convention.util-module"
            implementationClass = "space.getlike.convention.util.UtilModulePlugin"
        }
    }
}