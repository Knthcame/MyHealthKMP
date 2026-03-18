import dev.detekt.gradle.Detekt
import dev.detekt.gradle.DetektCreateBaselineTask
import dev.mokkery.MockMode
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.swiftexport.ExperimentalSwiftExportDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinXSerialization)
    alias(libs.plugins.kover)
    alias(libs.plugins.mokkery)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.detekt)
}

kotlin {
    android {
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

    iosArm64()
    iosSimulatorArm64()

    @OptIn(ExperimentalSwiftExportDsl::class)
    swiftExport {
        moduleName = "Shared"
        flattenPackage = "com.knthcame.myhealthkmp"
        configure {
            freeCompilerArgs.add("-Xexpect-actual-classes")
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
        implementation(libs.kotlinx.coroutines)
        implementation(libs.kotlinx.datetime)
        implementation(libs.kotlinx.serialization.json)
        implementation(libs.koin.core)
        implementation(libs.koin.compose)
        implementation(libs.koin.compose.viewmodel)
        implementation(libs.koin.compose.viewmodel.navigation)
        implementation(libs.androidx.room.runtime)
        implementation(libs.androidx.sqlite.bundled)

        testImplementation(libs.kotlin.test)
        testImplementation(libs.kotlinx.coroutines.test)
        testImplementation(libs.turbine)
    }

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
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
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
    add("kspDesktop", libs.androidx.room.compiler)
}

room {
    schemaDirectory("$projectDir/schemas")
}

mokkery {
    defaultMockMode.set(MockMode.autoUnit)
}
