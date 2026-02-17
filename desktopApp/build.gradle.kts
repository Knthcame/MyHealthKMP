import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    dependencies {
        implementation(projects.shared)

        implementation(compose.desktop.currentOs)
        implementation(libs.kotlinx.coroutines.swing)
        implementation(libs.koin.core)

        testImplementation(libs.koin.test)
        testImplementation(libs.koin.test.junit4)
    }
}

compose.desktop {
    application {
        mainClass = "com.knthcame.myhealthkmp.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.knthcame.myhealthkmp"
            packageVersion = "1.0.0"
        }
    }
}
