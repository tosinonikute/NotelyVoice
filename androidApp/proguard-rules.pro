# proguardFiles setting in build.gradle.

# Keep all public classes and their public and protected methods
-keep public class * {
    public protected *;
}

# Keep Hilt components
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.lifecycle.HiltViewModel { *; }

# Keep Compose classes
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# Keep Kotlin coroutines
-keep class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.coroutines.**

# Keep your data classes and models
-keep class com.module.notelycompose.** { *; }

# Keep Kotlin metadata
-keep class kotlin.Metadata { *; }

# Keep enum classes
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep Parcelable implementations
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# Additional rules for your specific dependencies
-keep class com.module.notelycompose.android.** { *; }

# Keep shared module classes
-keep class com.module.** { *; }

# DateTime serialization
-keep class kotlinx.datetime.** { *; }
-dontwarn kotlinx.datetime.**

# Navigation Compose
-keep class androidx.navigation.** { *; }

# This is generated automatically by the Android Gradle plugin.
-dontwarn java.awt.Component
-dontwarn java.awt.GraphicsEnvironment
-dontwarn java.awt.HeadlessException
-dontwarn java.awt.Window
-dontwarn javax.lang.model.element.Modifier

# JNA (Java Native Access) - Required for Vosk
-keep class com.sun.jna.** { *; }
-keep class * implements com.sun.jna.** { *; }
-keepclassmembers class * extends com.sun.jna.** {
    <methods>;
}
-dontwarn com.sun.jna.**

# Vosk speech recognition
-keep class org.vosk.** { *; }
-keep class org.vosk.android.** { *; }
-keepclassmembers class org.vosk.** {
    *;
}
-dontwarn org.vosk.**

# Keep native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep models module classes
-keep class **.models.** { *; }
-dontwarn **.models.**