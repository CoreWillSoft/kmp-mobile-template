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
            baseName = "OssConfigurator"
        }
    }

    sourceSets {
        /*
        Source sets structure
        common
         ├─ android
         ├─ ios
             ├─ iosX64
             ├─ iosArm64
             ├─ iosSimulatorArm64
         */
        val commonMain by getting {
            dependencies {
                //Network
                implementation(Deps.IO.Ktor.CORE)
                implementation(Deps.IO.Ktor.LOGGING)
                //Coroutines
                implementation(Deps.Core.Coroutine.CORE)
                //JSON
                implementation(Deps.IO.KotlinxSerialization.JSON)
                //Key-Value storage
                implementation(Deps.Storage.Settings.CORE)
                //Database
                implementation(Deps.Storage.SqlDelight.RUNTIME)
                // DI
                api(Deps.Di.CORE)
                api(Deps.Di.CORE_TEST)
            }
        }

        val androidMain by getting {
            dependencies {
                //Network
                implementation(Deps.IO.Ktor.OKHTTP)
                //Database
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
                //Network
                implementation(Deps.IO.Ktor.DARWIN)
                //Database
                implementation(Deps.Storage.SqlDelight.DRIVER_NATIVE)
            }
        }
    }
}

android {
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    compileSdk = AppCoordinates.Sdk.COMPILE_SDK_VERSION

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
