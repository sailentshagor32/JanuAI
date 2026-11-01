plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}
android {
    namespace = "com.januai.app"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.januai.app"
        minSdk = 23
        targetSdk = 28
        versionCode = 1
        versionName = "0.1"
    }
}
