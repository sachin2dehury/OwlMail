import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class OwlMailAppPlugIn : Plugin<Project> {

    private val pluginList = listOf(
        "com.android.application",

        "org.jetbrains.kotlin.android",
        "kotlin-kapt",

        "com.google.dagger.hilt.android",

        "com.google.gms.google-services",

        "com.google.firebase.firebase-perf",
        "com.google.firebase.crashlytics",

        "io.gitlab.arturbosch.detekt",

        "org.jlleitschuh.gradle.ktlint",

        "com.google.devtools.ksp"
    )

    override fun apply(target: Project) {
        configurePlugins(target)
        configureAndroid(target)
    }

    private fun configureAndroid(project: Project) = with(project) {
        extensions.configure<ApplicationExtension> {
            compileSdk = AppConfig.CompileSdk

            defaultConfig {
                minSdk = AppConfig.MinSdk
                targetSdk = AppConfig.TargetSdk
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            buildTypes {
                release {
                    isMinifyEnabled = true
                    isShrinkResources = true
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro"
                    )
                }
                debug {
//                    applicationIdSuffix = "debug"
                }
//                benchmark {
//                    // Enable all the optimizations from release build through initWith(release).
//                    initWith(release)
//                    matchingFallbacks.add("release")
//                    // Debug key signing is available to everyone.
//                    signingConfig = signingConfigs.getByName("debug")
//                    // Only use benchmark proguard rules
//                    proguardFiles("benchmark-rules.pro")
//                    isMinifyEnabled = true
//                    applicationIdSuffix = "benchmark"
//                }
            }
            compileOptions {
                sourceCompatibility(AppConfig.JDKVersion)
                targetCompatibility(AppConfig.JDKVersion)
            }
            viewBinding {
                enable = true
            }
        }
    }

    private fun configurePlugins(project: Project) = with(project.pluginManager) {
        pluginList.forEach { apply(it) }
    }
}
