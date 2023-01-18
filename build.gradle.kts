import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    dependencies {
        classpath("com.squareup.sqldelight:gradle-plugin:${Deps.Storage.SqlDelight.VERSION}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${Deps.Presentation.Navigation.VERSION}")
    }
}

plugins {
    id("com.android.application") version BuildPluginsVersions.AGP apply false
    id("com.android.library") version BuildPluginsVersions.AGP apply false
    kotlin("android") version BuildPluginsVersions.KOTLIN apply false
    kotlin("multiplatform") version BuildPluginsVersions.KOTLIN apply false
    kotlin("plugin.serialization") version BuildPluginsVersions.KOTLIN
    id("io.gitlab.arturbosch.detekt") version BuildPluginsVersions.DETEKT
    id("org.jlleitschuh.gradle.ktlint") version BuildPluginsVersions.KTLINT.PLUGIN
    id("com.github.ben-manes.versions") version BuildPluginsVersions.DEPENDENCY_UPDATES
}

/* Shared Infrastructure */

allprojects {
    group = PUBLISHING_GROUP
    repositories {
        google()
        mavenCentral()
    }
}

subprojects {
    tasks.withType<KotlinCompile>().all {
        val isReleaseTask = this.name.contains("release", ignoreCase = true)
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_1_8.toString()
            freeCompilerArgs = listOf(
                "-opt-in=kotlinx.coroutines.FlowPreview"
            )
            if (isReleaseTask) allWarningsAsErrors = true
        }
    }
    plugins.withType<JavaBasePlugin> {
        configure<JavaPluginExtension> {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
    }
    tasks.withType<Test>().all {
        useJUnitPlatform()
        systemProperty("gradle.build.dir", buildDir) // required by kotest
    }

    apply {
        plugin("io.gitlab.arturbosch.detekt")
        plugin("org.jlleitschuh.gradle.ktlint")
    }

    ktlint {
        debug.set(false)
        version.set(BuildPluginsVersions.KTLINT.CONFIG)
        verbose.set(true)
        android.set(false)
        outputToConsole.set(true)
        ignoreFailures.set(false)
        enableExperimentalRules.set(true)
        reporters {
            reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
        }
        filter {
            exclude("**/generated/**")
            include("**/kotlin/**")
        }
    }

    detekt {
        config = rootProject.files("config/quality/detekt/detekt.yml")
        reports {
            xml {
                enabled = true
            }
            html {
                enabled = true
                destination = file("build/reports/detekt.html")
            }
        }
    }
    tasks {
        withType<io.gitlab.arturbosch.detekt.Detekt> {
            this.jvmTarget = "1.8"
        }
    }
}

tasks.withType<DependencyUpdatesTask> {
    fun isNonStable(version: String) = "^[0-9,.v-]+(-r)?$".toRegex().matches(version).not()
    rejectVersionIf {
        isNonStable(candidate.version)
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
