plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization")
    id("com.squareup.sqldelight")
}

kotlin {
    android()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                // Coroutines
                implementation(Deps.Core.Coroutine.CORE)
                // Logger
                implementation(Deps.Logging.Napier.CORE)
                // JSON
                implementation(Deps.IO.KotlinxSerialization.JSON)
                // Database
                implementation(Deps.Storage.SqlDelight.RUNTIME)
                // Key-Value storage
                implementation(Deps.Storage.Settings.CORE)
                // DateTime
                implementation(Deps.Util.DATE_TIME)
                // DI
                api(Deps.Di.CORE)
            }
        }
        val commonTest by getting
        val androidMain by getting {
            dependencies {
                implementation(Deps.Storage.SqlDelight.DRIVER_ANDROID)
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation(Deps.Storage.SqlDelight.DRIVER_NATIVE)
            }
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    compileSdk = AppCoordinates.Sdk.COMPILE_SDK_VERSION
    namespace = "${AppCoordinates.APP_ID}.shared"

    defaultConfig {
        minSdk = AppCoordinates.Sdk.MIN_SDK_VERSION
        targetSdk = AppCoordinates.Sdk.TARGET_SDK_VERSION
    }
    compileOptions {
        // Flag to enable support for the new language APIs
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    dependencies {
        coreLibraryDesugaring(Deps.Util.DESUGAR)
    }
}

sqldelight {
    database("AppDatabase") {
        packageName = "${AppCoordinates.APP_ID}.shared.cache"
    }
}
