plugins {
    kotlin("multiplatform").apply(false)
    id("com.android.application").apply(false)
    id("com.android.library").apply(false)
    id("org.jetbrains.compose").apply(false)
}
buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.sqldelight.gradle.plugin)
        classpath(libs.hilt.android.gradle.plugin)
        classpath(libs.atomicfu.gradle.plugin)
    }
}
allprojects {
    apply(plugin = "kotlinx-atomicfu")
}
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
