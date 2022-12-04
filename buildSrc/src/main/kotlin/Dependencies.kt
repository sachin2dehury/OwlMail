object Dependencies {

    // Core
    const val CoreKTX = "androidx.core:core-ktx:${DependencyVersion.CoreKTX}"
    const val AppCompat = "androidx.appcompat:appcompat:${DependencyVersion.AppCompat}"

    // Ui
    const val Material = "com.google.android.material:material:${DependencyVersion.Material}"
    const val ConstraintLayout =
        "androidx.constraintlayout:constraintlayout:${DependencyVersion.ConstraintLayout}"

    // Hilt
    const val DaggerHilt = "com.google.dagger:hilt-android:${DependencyVersion.DaggerHilt}"
    const val DaggerHiltCompiler =
        "com.google.dagger:hilt-android-compiler:${DependencyVersion.DaggerHilt}"

    // Firebase
    const val FirebaseBom = "com.google.firebase:firebase-bom:${DependencyVersion.FirebaseBom}"
    const val FirebasePerformance = "com.google.firebase:firebase-perf-ktx"
    const val FirebaseRemoteConfig = "com.google.firebase:firebase-config-ktx"
    const val FirebaseAnalytics = "com.google.firebase:firebase-analytics-ktx"
    const val FirebaseCrashlytics = "com.google.firebase:firebase-crashlytics-ktx"

    // Coroutines
    const val CoroutineCore =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${DependencyVersion.Coroutines}"
    const val CoroutineAndroid =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${DependencyVersion.Coroutines}"

    // Retrofit
    const val Retrofit = "com.squareup.retrofit2:retrofit:${DependencyVersion.Retrofit}"

    // Moshi
    const val Moshi = "com.squareup.moshi:moshi-kotlin:${DependencyVersion.Moshi}"
    const val MoshiRetrofitConverter =
        "com.squareup.retrofit2:converter-moshi:${DependencyVersion.Retrofit}"
    const val MoshiCodegen = "com.squareup.moshi:moshi-kotlin-codegen:${DependencyVersion.Moshi}"

    // OkHttp
    const val OkHttp = "com.squareup.okhttp3:okhttp:${DependencyVersion.OkHttp}"

    // Stetho
    const val Stetho = "com.facebook.stetho:stetho:${DependencyVersion.Stetho}"

    // Chucker
    const val Chucker = "com.github.chuckerteam.chucker:library:${DependencyVersion.Chucker}"
    const val ChuckerNoOP =
        "com.github.chuckerteam.chucker:library-no-op:${DependencyVersion.Chucker}"

    // Room
    const val Room = "androidx.room:room-ktx:${DependencyVersion.Room}"
    const val RoomCompiler = "androidx.room:room-compiler:${DependencyVersion.Room}"

    // Preferences DataStore
    const val DataStore = "androidx.datastore:datastore-preferences:${DependencyVersion.DataStore}"

    // Test
    const val Junit = "junit:junit:${DependencyVersion.Junit}"
    const val JunitAndroid = "androidx.test.ext:junit:${DependencyVersion.JunitAndroid}"
    const val EspressoCore = "androidx.test.espresso:espresso-core:${DependencyVersion.Espresso}"

    // Lint
    const val Lint = "com.android.tools.lint:lint-api:${DependencyVersion.Lint}"
}
