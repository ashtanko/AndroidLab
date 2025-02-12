plugins {
    alias(libs.plugins.androidlab.android.library)
    alias(libs.plugins.androidlab.android.library.compose)
    alias(libs.plugins.androidlab.android.library.jacoco)
    alias(libs.plugins.roborazzi)
}

android {
    namespace = "dev.shtanko.androidlab.core.designsystem"
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    libs.apply {
        androidx.apply {
            compose.apply {
                api(foundation)
                api(foundation.layout)
                api(icons.extended)
                api(material3.adaptive)
                api(material3.adaptive.layout)
                api(material3.adaptive.navigation)
                api(material3.adaptive.navigationSuite)
                api(materialWindow)
            }
            ui.apply {
                api(text.google.fonts)
                debugApi(test.manifest)
                androidTestApi(test.junit4)
                androidTestApi(test)
            }
        }
    }
}
