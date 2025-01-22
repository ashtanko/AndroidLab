import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetContainer

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.detekt)
    alias(libs.plugins.compose.guard)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.serialization)
    alias(libs.plugins.kover)
    alias(libs.plugins.ksp)
    alias(libs.plugins.sonarqube)
    alias(libs.plugins.screenshot)
    jacoco
}

android {
    namespace = "dev.shtanko.androidlab"
    compileSdk = 35

    defaultConfig {
        applicationId = "dev.shtanko.androidlab"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility(libs.versions.jvmTarget.get())
        targetCompatibility(libs.versions.jvmTarget.get())
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            merges += "META-INF/LICENSE.md"
            merges += "META-INF/LICENSE-notice.md"
        }
    }
    testOptions {
        // Required for Robolectric
        unitTests.isIncludeAndroidResources = true
        unitTests.isReturnDefaultValues = true

        unitTests.all {
            it.useJUnitPlatform()
            it.jvmArgs(
                "--add-opens",
                "java.base/java.util=ALL-UNNAMED",
                "--add-opens",
                "java.base/java.lang=ALL-UNNAMED",
                "--add-opens",
                "java.base/java.time=ALL-UNNAMED",
                "-Xshare:off",
            )
        }
        screenshotTests {
            imageDifferenceThreshold = 0.0001f // 0.01%
        }
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
    experimentalProperties["android.experimental.enableScreenshotTest"] = true
}

project.gradle.startParameter.excludedTaskNames.add("testDebugScreenshotTest")
project.gradle.startParameter.excludedTaskNames.add("testReleaseScreenshotTest")

tasks {
    getByName("check") {
        // Add detekt with type resolution to check
        dependsOn("detekt")
    }

    getByName("sonar") {
        dependsOn("check")
    }

    withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        jvmTarget = libs.versions.jvmTarget.get()
    }

    withType<io.gitlab.arturbosch.detekt.DetektCreateBaselineTask>().configureEach {
        jvmTarget = libs.versions.jvmTarget.get()
    }

    withType<Test> {
        useJUnitPlatform()
        maxHeapSize = "2g"
        maxParallelForks = Runtime.getRuntime().availableProcessors()
        jvmArgs = jvmArgs.orEmpty() + "-XX:+UseParallelGC"
        android.sourceSets["main"].res.srcDirs("src/test/res")
        jvmArgs(
            "--add-opens",
            "java.base/java.util=ALL-UNNAMED",
            "--add-opens",
            "java.base/java.lang=ALL-UNNAMED",
            "--add-opens",
            "java.base/java.time=ALL-UNNAMED",
            "-Xshare:off",
        )
    }

    register<JacocoReport>("testCoverage") {
        dependsOn("test")
        group = "Reporting"
        description = "Generate Jacoco coverage reports"

        val excludedFiles = mutableSetOf("**/*Test*.*")
        val projectBuildDirectory = project.layout.buildDirectory.get().asFile.absoluteFile
        val sourceDirs = fileTree(
            "$projectBuildDirectory/classes/kotlin/",
        ) {
            exclude(excludedFiles)
        }
        val coverageDirs = listOf(
            "src/main/java",
            "src/main/kotlin",
        )
        classDirectories.setFrom(files(sourceDirs))
        additionalClassDirs.setFrom(files(coverageDirs))
        executionData.setFrom(
            files("$projectBuildDirectory/jacoco/test.exec")
        )

        reports {
            listOf(xml, html).map { it.required }.forEach { it.set(true) }
            xml.outputLocation.set(file("$projectBuildDirectory/reports/jacoco/report.xml"))
        }
    }
}

composeGuardCheck {
    errorOnNewDynamicProperties = false
    errorOnNewUnstableClasses = false
    reportAllOnMissingBaseline = true
}

composeGuard {
    configureKotlinTasks = false
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(libs.versions.jvmTarget.get()))
    }
}

detekt {
    config.setFrom("${project.rootDir}/config/detekt/detekt.yml")

    val projectDir = projectDir
    val kotlinExtension = project.extensions.getByType(KotlinSourceSetContainer::class.java)

    source.setFrom(
        provider {
            kotlinExtension.sourceSets
                .flatMap { sourceSet ->
                    sourceSet.kotlin.srcDirs.filter {
                        it.relativeTo(projectDir).startsWith("src")
                    }
                }
        },
    )
}

configure<DetektExtension> {
    config.from("${project.rootDir}/config/detekt/detekt-compose.yml")
}

dependencies {
    libs.apply {
        androidx.apply {
            implementation(core.ktx)
            implementation(lifecycle.runtime.ktx)
            implementation(activity.compose)
            implementation(platform(compose.bom))
            implementation(ui)
            implementation(ui.graphics)
            implementation(ui.tooling.preview)
            implementation(material3)
            implementation(hilt.navigation.compose)

            androidTestImplementation(junit)
            // androidTestImplementation(espresso.core)
            // androidTestImplementation(espresso.contrib)
            androidTestImplementation(platform(compose.bom))
            androidTestImplementation(ui.test.junit4)

            debugImplementation(ui.tooling)
            debugImplementation(ui.test.manifest)

            ui.apply {
                debugImplementation(test.manifest)
                androidTestImplementation(test.junit4)
                androidTestImplementation(test)
            }

            androidTestImplementation(arch.core.test)
            screenshotTestImplementation(compose.ui.tooling)
        }

        kotlinx.apply {
            implementation(collections.immutable)
            implementation(coroutines.android)
            implementation(coroutines.core)
            implementation(serialization)

            testImplementation(coroutines.test)
            testImplementation(coroutines.debug)
        }

        detekt.apply {
            detektPlugins(rules)
            detektPlugins(formatting)
        }

        google.hilt.apply {
            implementation(android)
            ksp(compiler)
            kspTest(compiler)
            kspAndroidTest(compiler)
            testImplementation(android.testing)
        }

        implementation(room.runtime)
        implementation(room.ktx)
        implementation(room.paging)
        ksp(room.compiler)

        implementation(jacoco.core)

        implementation(square.okhttp)
        implementation(square.okhttp.logging)
        implementation(square.okhttp.mockwebserver)
        implementation(square.retrofit.core)
        implementation(skydoves.sandwich.retrofit)
        implementation(square.retrofit.kotlin.serialization)

        testImplementation(square.turbine)
        testImplementation(mockito)
        testImplementation(mockito.kotlin2)
        testImplementation(mockk.kotlin)
        androidTestImplementation(mockk.android)

        // testImplementation(robolectric.robolectric)
        // androidTestImplementation(robolectric.robolectric)

        junit5.apply {
            testImplementation(api)
            testImplementation(params)

            androidTestImplementation(api)
            androidTestImplementation(params)

            testRuntimeOnly(jupiterEngine)
            testRuntimeOnly(vintageEngine)
        }
    }
}
