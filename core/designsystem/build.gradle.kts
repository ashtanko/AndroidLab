plugins {
    alias(libs.plugins.androidlab.android.library)
    alias(libs.plugins.androidlab.android.library.compose)
    alias(libs.plugins.androidlab.android.library.jacoco)
    alias(libs.plugins.roborazzi)
}

android {
    namespace = "dev.shtanko.androidlab.core.designsystem"
}

dependencies {
    libs.apply {
        androidx.apply {

            compose.apply {
                api(foundation)
                api(foundation.layout)
                api(runtime)
                api(ui.util)
                api(icons.extended)
                api(materialWindow)
            }

            compose.material3.adaptive.apply {
                api(this)
                api(layout)
                api(navigation)
                api(navigationSuite)
            }

            ui.apply {
                api(text.google.fonts)
                debugApi(test.manifest)
                androidTestApi(test.junit4)
                androidTestApi(test)
            }
        }

        testImplementation(libs.hilt.android.testing)
        testImplementation(libs.robolectric.robolectric)
        testImplementation(project(":core:screenshot-testing"))
    }
}
