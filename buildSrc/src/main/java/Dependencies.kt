const val kotlinVersion = "1.4.0-rc"
const val navigation = "2.3.0-alpha03"

object Modules {
    const val core = ":core"
    const val coreUi = ":core_ui"
    const val coreNetwork = ":core_network"
    const val coreNetworkAuth = ":core_network_auth"
    const val coreNetworkNoAuth = ":core_network_no_auth"
    const val coreLocalization = ":core_localization"
    const val featureLogin = ":feature_login"
    const val featureMain = ":feature_main"
    const val featureDashboard = ":feature_dashboard"
    const val featureCustomers = ":feature_customers"
    const val featurePayment = ":feature_payment"
    const val featureAppliance = ":feature_appliance"
    const val featureTicket = ":feature_ticket"
    const val sharedNavigation = ":shared_navigation"
    const val sharedAgent = ":shared_agent"
    const val sharedSuccess = ":shared_success"
    const val sharedCustomer = ":shared_customer"
    const val sharedMessaging = ":shared_messaging"
}

object BuildPlugins {

    object Versions {
        const val buildToolsVersion = "4.0.1"
        const val gradleVersionPlugin = "0.27.0"
        const val googleServices = "4.3.3"
        const val firebaseCrashlytics = "2.2.0"
    }

    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.buildToolsVersion}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
    const val androidApplication = "com.android.application"
    const val androidLibrary = "com.android.library"
    const val kotlinAndroid = "kotlin-android"
    const val kotlinAndroidExtensions = "kotlin-android-extensions"
    const val kotlinKapt = "kotlin-kapt"
    const val gradleVersionsPlugin = "com.github.ben-manes:gradle-versions-plugin:${Versions.gradleVersionPlugin}"
    const val safeArgsPlugin = "androidx.navigation:navigation-safe-args-gradle-plugin:${navigation}"
    const val googleServices = "com.google.gms:google-services:${Versions.googleServices}"
    const val googleServicesBuild = "com.google.gms.google-services"
    const val firebaseCrashlytics = "com.google.firebase:firebase-crashlytics-gradle:${Versions.firebaseCrashlytics}"
    const val firebaseCrashlyticsBuild = "com.google.firebase.crashlytics"
    const val gradleVersionsBuild = "com.github.ben-manes.versions"
}

object AndroidSdk {
    const val min = 21
    const val compile = 29
    const val target = 29
}

object Libraries {
    private object Versions {
        const val jetpack = "1.0.0-beta01"
        const val ktx = "1.1.0-alpha05"
        const val koin = "2.1.6"
        const val fragmentKtx = "1.2.0-rc01"
        const val preference = "1.1.1"
        const val material = "1.2.0-alpha05"
        const val constraintLayout = "2.0.0-beta4"
        const val recyclerView = "1.1.0"
        const val paris = "1.4.0"
        const val mpAndroidChart = "v3.1.0"
        const val retrofit = "2.7.1"
        const val okhttpLogging = "4.3.0"
        const val rxAndroid = "2.1.1"
        const val rxJava = "2.2.9"
        const val rxKotlin = "2.3.0"
        const val rxBinding = "2.2.0"
        const val timber = "4.7.1"
        const val lottie = "3.3.1"
        const val textDrawable = "558677ea316e60346948b381e5e274f49b00d370"
        const val firebaseCrashlytics = "17.1.1"
        const val firebaseMessaging = "20.2.3"
        const val firebaseAnalytics = "17.4.4"
    }

    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion"
    const val ktxCore = "androidx.core:core-ktx:${Versions.ktx}"

    const val koinScope = "io.insert-koin:koin-androidx-scope:${Versions.koin}"
    const val koinViewModel = "io.insert-koin:koin-androidx-viewmodel:${Versions.koin}"
    const val koinCore = "io.insert-koin:koin-core:${Versions.koin}"
    const val koinExt = "io.insert-koin:koin-core-ext:${Versions.koin}"

    const val fragmentKtx = "androidx.fragment:fragment-ktx:${Versions.fragmentKtx}"
    const val preferenceKtx = "androidx.preference:preference-ktx:${Versions.preference}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.jetpack}"
    const val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerView}"
    const val navigationFragment = "androidx.navigation:navigation-fragment-ktx:${navigation}"
    const val navigationUi = "androidx.navigation:navigation-ui-ktx:${navigation}"
    const val paris = "com.airbnb.android:paris:${Versions.paris}"
    const val mpAndroidChart = "com.github.PhilJay:MPAndroidChart:${Versions.mpAndroidChart}"

    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val retrofitGsonConverter = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    const val retrofitAdapter = "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofit}"
    const val okhttpLogging = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttpLogging}"

    const val rxAndroid = "io.reactivex.rxjava2:rxandroid:${Versions.rxAndroid}"
    const val rxJava = "io.reactivex.rxjava2:rxjava:${Versions.rxJava}"
    const val rxKotlin = "io.reactivex.rxjava2:rxkotlin:${Versions.rxKotlin}"
    const val rxBinding = "com.jakewharton.rxbinding2:rxbinding:${Versions.rxBinding}"

    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    const val lottie = "com.airbnb.android:lottie:${Versions.lottie}"

    const val firebaseCrashlytics = "com.google.firebase:firebase-crashlytics:${Versions.firebaseCrashlytics}"
    const val firebaseMessaging = "com.google.firebase:firebase-messaging:${Versions.firebaseMessaging}"
    const val firebaseAnalytics = "com.google.firebase:firebase-analytics-ktx:${Versions.firebaseAnalytics}"

    const val textDrawable = "com.github.amulyakhare:TextDrawable:${Versions.textDrawable}"
}

object TestLibraries {
    private object Versions {
        const val junit4 = "4.13"
        const val testRunner = "1.3.0-rc03"
        const val espresso = "3.3.0-rc03"
    }

    const val junit4 = "junit:junit:${Versions.junit4}"
    const val testRunner = "androidx.test:runner:${Versions.testRunner}"
    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
}