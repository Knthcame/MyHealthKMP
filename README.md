[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Knthcame_MyHealthKMP&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=Knthcame_MyHealthKMP)

[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=Knthcame_MyHealthKMP&metric=coverage)](https://sonarcloud.io/summary/new_code?id=Knthcame_MyHealthKMP)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=Knthcame_MyHealthKMP&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=Knthcame_MyHealthKMP)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=Knthcame_MyHealthKMP&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=Knthcame_MyHealthKMP)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=Knthcame_MyHealthKMP&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=Knthcame_MyHealthKMP)

# MyHealthKMP
A sample Kotlin Multiplatform project for Android and iOS that shows how to implement common features of mobile applications.

The following libraries are used:
- [Compose multiplatform](https://www.jetbrains.com/compose-multiplatform/) for sharing the UI across platforms
- [Kotlin-test](https://kotlinlang.org/api/core/kotlin-test/) for unit tests.
- [JUnit4](https://junit.org/junit4/) for instrumented tests on Android
  - To be migrated to [Compose Multiplatform UI tests](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-test.html) when it's a bit more stable (currently experimental)
- [Kover](https://github.com/Kotlin/kotlinx-kover) for code coverage of Kotlin code
  - At the moment, Kover does not provide coverage of Kotlin/Native targets. So, they are excluded from coverage in the sonar configuration.

## Project structure
This is a Kotlin Multiplatform project targeting Android, iOS, Desktop.

* `/shared` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

* `/androidApp` contains the android app module, and serves as the entry point for the Android app.

* `/desktopApp` contains the desktop/jvm app module, and serves as the entry point for the desktop app.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform, 
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.


Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…
