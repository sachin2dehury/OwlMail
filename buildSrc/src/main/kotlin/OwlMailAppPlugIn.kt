import org.gradle.api.Plugin
import org.gradle.api.Project

class OwlMailAppPlugIn : Plugin<Project> {

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
        configureLint(target)
        configureBuildFeatures(target)
        configureJVM(target)
    }

    private fun configureJVM(project: Project) {
//        project.extensions.getByType<BaseExtention>()
    }

    private fun configureBuildFeatures(project: Project) {
    }

    private fun configureLint(project: Project) {
    }

    private fun configurePlugins(project: Project) {
        pluginList.forEach {
            project.plugins.apply(it)
        }
    }
}
