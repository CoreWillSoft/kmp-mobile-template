pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = ("kmp-mobile-template")

include(
    ":androidApp",
    ":shared",
    "library-android"
)
