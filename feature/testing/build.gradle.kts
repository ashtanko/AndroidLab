plugins {
    alias(libs.plugins.androidlab.android.library)
    alias(libs.plugins.androidlab.hilt)
}

android {
    namespace = "dev.shtanko.androidlab.core.designsystem.feature.testing"
}

dependencies {
    api(libs.kotlinx.coroutines.test)

    implementation(libs.androidx.test.rules)
    implementation(libs.hilt.android.testing)
    implementation(libs.kotlinx.datetime)
}
