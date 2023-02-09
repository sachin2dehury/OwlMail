plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
}

dependencies {
    compileOnly("com.android.tools.build:gradle:7.3.1")
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.20")
}

gradlePlugin {
    plugins {
        register("OwlMailApp") {
            id = "owl-mail-app"
            implementationClass = "OwlMailAppPlugIn"
        }
//        register("OwlMailLibrary") {
//            id = "owl-mail-library"
//            implementationClass = "OwlMailLibraryPlugin"
//        }
//        register("OwlMailFeature") {
//            id = "owl-mail-feature"
//            implementationClass = "OwlMailFeaturePlugin"
//        }
    }
}
