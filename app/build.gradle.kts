plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = AppConfig.namespace
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        applicationId = AppConfig.applicationId
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName

        testInstrumentationRunner = AppConfig.androidTestInstrumentation
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        jvmToolchain(11)
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = AppConfig.composeVersion
    }
    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
            excludes.add("META-INF/LICENSE.md")
            excludes.add("META-INF/LICENSE-notice.md")
        }
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtimeKtx)
    implementation(libs.androidx.activity.compose)

    implementation(libs.androidx.appcompat)
    implementation(libs.timber)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.dagger.hilt)
    implementation(libs.androidx.hilt.navigation.compose)
    kapt(libs.dagger.hilt.compiler)

    debugImplementation(libs.androidx.compose.ui.tooling)

    // Required to add androidx.activity.ComponentActivity to test manifest
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    // Used to create AndroidHiltTestRunner from AndroidJUnitRunner
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    // Needed for createAndroidComposeRule and other rules used to perform UI test
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    // Dependency injection for For instrumented tests
    androidTestImplementation(libs.dagger.hilt.android.testing)
    kaptAndroidTest(libs.dagger.hilt.compiler)

    // Allows us to create and configure mock objects, stub methods, verify method invocations, and more
    androidTestImplementation(libs.mockk.agent)
    androidTestImplementation(libs.mockk.android)
    // Assertion library
    androidTestImplementation(libs.truth)

    // Regular JUnit dependency
    testImplementation(libs.junit4)
    /* Needed for createAndroidComposeRule and other rules used to perform UI test - here we use robolectric to run ui
     test on jvm */
    testImplementation(libs.androidx.compose.ui.test.junit4)
    // Needed to run android UI test on JVM instead of on an emulator or device
    testImplementation(libs.robolectric)
    // Allows us to create and configure mock objects, stub methods, verify method invocations, and more
    testImplementation(libs.mockk)
    // Assertion library
    testImplementation(libs.truth)
    // Dependency injection for For instrumented tests
    testImplementation(libs.dagger.hilt.android.testing)
    kaptTest(libs.dagger.hilt.compiler)

    // To perform UI automation test.
    androidTestImplementation(libs.androidx.test.uiautomator)
}