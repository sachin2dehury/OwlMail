import com.android.build.api.dsl.ApplicationExtension
import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class OwlMailFeaturePlugIn : Plugin<Project> {

    private val pluginList = listOf(
        Plugins.android,
        Plugins.kapt,
        Plugins.hiltAndroid,
        Plugins.playServices,
        Plugins.firebasePerformance,
        Plugins.firebaseCrashlytics
    )

    override fun apply(target: Project) {
        configurePlugins(target)
//        configureBuildFeatures(target)
//        configureJVM(target)
    }

    private fun configureJVM(project: Project) {
        with(project.extensions.getByType<BaseExtension>()) {
            project.tasks.withType(KotlinCompile::class.java).all {
                compileOptions {
                    sourceCompatibility(AppConfig.JDKVersion)
                    targetCompatibility(AppConfig.JDKVersion)
                    kotlinOptions.languageVersion = AppConfig.KotlinLanguageVersion
                }
                kotlinOptions {
                    jvmTarget = AppConfig.JvmTargetVersion
                }
            }
        }
    }

    private fun configureBuildFeatures(project: Project) {
        project.extensions.getByType<ApplicationExtension>().run {
            viewBinding {
                enable = true
            }
        }
    }

    private fun configurePlugins(project: Project) {
        pluginList.forEach {
            project.plugins.apply(it)
        }
    }
}
