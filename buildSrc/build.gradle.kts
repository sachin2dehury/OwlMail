plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
}

dependencies {
    implementation("com.android.tools.build:gradle:7.4.2")
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.21")
    gradleApi()
}

gradlePlugin {
    plugins {
        register("OwlMailApp") {
            id = "owl-mail-app"
            implementationClass = "OwlMailAppPlugIn"
        }
        register("OwlMailLibrary") {
            id = "owl-mail-library"
            implementationClass = "OwlMailLibraryPlugin"
        }
        register("OwlMailFeature") {
            id = "owl-mail-feature"
            implementationClass = "OwlMailFeaturePlugin"
        }
    }
}
