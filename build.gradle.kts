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
        classpath("com.squareup.sqldelight:gradle-plugin:1.5.5")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.44")
        classpath("org.jetbrains.kotlinx:atomicfu-gradle-plugin:0.17.3")
    }
}
allprojects {
    apply(plugin = "kotlinx-atomicfu")
}
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
