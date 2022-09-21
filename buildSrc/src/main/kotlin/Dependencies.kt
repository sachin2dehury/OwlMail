import org.gradle.internal.impldep.com.google.api.services.storage.model.Bucket

object Dependencies {


    const val CoreKTX = "androidx.core:core-ktx:${DependencyVersion.CoreKTX}"
    const val AppCompat = "androidx.appcompat:appcompat:${DependencyVersion.AppCompat}"

    const val Material = "com.google.android.material:material:${DependencyVersion.Material}"
    const val ConstraintLayout =
        "androidx.constraintlayout:constraintlayout:${DependencyVersion.ConstraintLayout}"

    const val Junit = "junit:junit:${DependencyVersion.Junit}"
    const val JunitAndroid = "androidx.test.ext:junit:${DependencyVersion.JunitAndroid}"
    const val EspressoCore = "androidx.test.espresso:espresso-core:${DependencyVersion.Espresso}"


    const val DaggerHilt = "com.google.dagger:hilt-android:${DependencyVersion.DaggerHilt}"
    const val DaggerHiltCompiler =
        "com.google.dagger:hilt-android-compiler:${DependencyVersion.DaggerHilt}"
    const val DaggerHiltClassPath = "com.google.dagger:hilt-android-gradle-plugin:${DependencyVersion.DaggerHilt}"


    const val ViewModelActivityKTX = "androidx.activity:activity-ktx:${DependencyVersion.ViewModelActivityKTX}"

    const val LifecycleKTX = "androidx.lifecycle:lifecycle-viewmodel-ktx:${DependencyVersion.LifecycleKTX}"

    const val Lifecycle = "androidx.lifecycle:lifecycle-extensions:${DependencyVersion.Lifecycle}"
    const val Livedata = "androidx.lifecycle:lifecycle-livedata-ktx:${DependencyVersion.LifecycleKTX}"
    const val Runtime = "androidx.lifecycle:lifecycle-runtime-ktx:${DependencyVersion.LifecycleKTX}"

    // Retrofit
    const val Retrofit2 = "com.squareup.retrofit2:retrofit:${DependencyVersion.Retrofit}"
    const val ConverterMoshi = "com.squareup.retrofit2:converter-moshi:${DependencyVersion.Retrofit}"
    const val OkHttp = "com.squareup.okhttp3:okhttp:${DependencyVersion.OkHttp}"

    const val Paging = "androidx.paging:paging-common-ktx:${DependencyVersion.Paging}"

    const val NavFragment = "androidx.navigation:navigation-fragment-ktx:${DependencyVersion.Navigation}"
    const val NavUI = "androidx.navigation:navigation-ui-ktx:${DependencyVersion.Navigation}"
    const val NavDynamicSupport = "androidx.navigation:navigation-dynamic-features-fragment:${DependencyVersion.Navigation}"

    const val Moshi = "com.squareup.moshi:moshi-kotlin:${DependencyVersion.Moshi}"
    const val MoshiKapt = "com.squareup.moshi:moshi-kotlin-codegen:${DependencyVersion.Moshi}"
}