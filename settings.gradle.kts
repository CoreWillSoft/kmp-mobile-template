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
rootProject.name = ("template-app")

include(
    ":androidApp",
    ":shared",
    "library-android",
    "library-kotlin",
)
