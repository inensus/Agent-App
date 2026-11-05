plugins {
    id(BuildPlugins.androidLibrary)
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.kotlinAndroidExtensions)
}

android {
    compileSdkVersion(AndroidSdk.compile)

    defaultConfig {
        minSdkVersion(AndroidSdk.min)
        targetSdkVersion(AndroidSdk.target)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "DEFAULT_BASE_URL", "\"https://cloud.micropowermanager.com/api/\"")
        }

        getByName("debug") {
            buildConfigField("String", "DEFAULT_BASE_URL", "\"http://api.mpmanager.local/api/\"")
        }
    }
}

dependencies {
    api(Libraries.kotlinStdLib)
    api(Libraries.ktxCore)
    api(Libraries.koinScope)
    api(Libraries.koinViewModel)
    api(Libraries.koinCore)
    api(Libraries.koinExt)
    api(Libraries.rxJava)
    api(Libraries.rxKotlin)
    api(Libraries.rxAndroid)
    api(Libraries.rxBinding)
    api(Libraries.timber)
    api(Libraries.preferenceKtx)
    api(Libraries.firebaseMessaging)
    api(Libraries.firebaseAnalytics)
    api(Libraries.firebaseCrashlytics)
}