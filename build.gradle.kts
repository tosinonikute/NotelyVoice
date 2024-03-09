plugins {
    kotlin("multiplatform").version("1.8.21").apply(false)
    id("com.android.application").version("8.1.0-beta01").apply(false)
    id("com.android.library").version("8.1.0-beta01").apply(false)
    kotlin("android").version("1.8.21").apply(false)
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
