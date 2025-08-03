plugins {
    alias(libs.plugins.androidlab.android.library)
    alias(libs.plugins.androidlab.android.library.compose)
    alias(libs.plugins.androidlab.hilt)
}

android {
    namespace = "dev.shtanko.androidlab.core.screenshottesting"
}

dependencies {
    api(libs.bundles.androidx.compose.ui.test)
    api(libs.roborazzi)
    api(libs.roborazzi.accessibility.check)
    implementation(libs.androidx.compose.ui.test)
    implementation(libs.androidx.activity.compose)
    implementation(libs.robolectric.robolectric)
    implementation(project(":core:designsystem"))
}
