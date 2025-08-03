plugins {
    alias(libs.plugins.androidlab.android.application)
    alias(libs.plugins.androidlab.android.application.compose)
}
android {
    defaultConfig {
        applicationId = "dev.shtanko.lab.catalog"
        versionCode = 1
        versionName = "0.0.1" // X.Y.Z; X = Major, Y = minor, Z = Patch level
    }

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
    namespace = "dev.shtanko.lab.catalog"

    buildTypes {
        release {
            // To publish on the Play store a private signing key is required, but to allow anyone
            // who clones the code to sign and run the release variant, use the debug signing key.
            // TODO: Abstract the signing configuration to a separate file to avoid hardcoding this.
            signingConfig = signingConfigs.named("debug").get()
        }
    }
}

dependencies {
    implementation(libs.androidx.activity.compose)

    implementation(project(":core:designsystem"))
}
