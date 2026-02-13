import dev.detekt.gradle.Detekt
import dev.detekt.gradle.DetektCreateBaselineTask
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinXSerialization)
    alias(libs.plugins.kover)
    alias(libs.plugins.skie)
    alias(libs.plugins.mokkery)
    alias(libs.plugins.detekt)
}

kotlin {
    androidLibrary {
        namespace = "com.knthcame.myhealthkmp.shared"
        compileSdk =
            libs.versions.android.compileSdk
                .get()
                .toInt()
        minSdk =
            libs.versions.android.minSdk
                .get()
                .toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
        androidResources {
            enable = true
        }
        withHostTest {
            isIncludeAndroidResources = true
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm("desktop")

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    dependencies {
        implementation(libs.jetbrains.compose.runtime)
        implementation(libs.jetbrains.compose.foundation)
        implementation(libs.jetbrains.compose.material3)
        implementation(libs.jetbrains.compose.ui)
        implementation(libs.jetbrains.components.resources)
        implementation(libs.jetbrains.compose.ui.tooling.preview)
        implementation(libs.androidx.lifecycle.viewmodel)
        implementation(libs.androidx.lifecycle.runtime.compose)
        implementation(libs.jetbrains.navigation.compose)
        implementation(libs.kotlinx.datetime)
        implementation(libs.kotlinx.serialization.json)
        implementation(libs.koin.core)
        implementation(libs.koin.compose)
        implementation(libs.koin.compose.viewmodel)
        implementation(libs.koin.compose.viewmodel.navigation)

        testImplementation(libs.kotlin.test)
        testImplementation(libs.kotlinx.coroutines.test)
        testImplementation(libs.turbine)
    }
}

detekt {
    buildUponDefaultConfig = true // preconfigure defaults
    allRules = false // activate all available (even unstable) rules.
    source =
        files(
            "src/androidHostTest/kotlin",
            "src/androidMain/kotlin",
            "src/commonMain/kotlin",
            "src/commonTest/kotlin",
            "src/desktopMain/kotlin",
            "src/desktopTest/kotlin",
            "src/iosMain/kotlin",
            "src/iosTest/kotlin",
        )
    config.setFrom("$projectDir/../config/detekt.yml") // point to your custom config defining rules to run, overwriting default behavior
    // baseline = file("$projectDir/../config/baseline.xml")
}
tasks.withType<Detekt>().configureEach {
    jvmTarget = JvmTarget.JVM_11.target
}
tasks.withType<DetektCreateBaselineTask>().configureEach {
    jvmTarget = JvmTarget.JVM_11.target
}

dependencies {
    androidRuntimeClasspath(libs.jetbrains.compose.ui.tooling)
}
