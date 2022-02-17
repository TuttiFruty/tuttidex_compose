import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("org.jetbrains.kotlin.kapt")
    id("kotlin-parcelize")
}

val compose_version = rootProject.extra.get("compose_version") as String
android {
    compileSdk = 31

    defaultConfig {
        applicationId = "fr.tuttifruty.pokeapp"
        minSdk = 21
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"

        buildConfigField("int", "VERSION_DB", "1")
        buildConfigField("String", "URL", "\"https://pokeapi.co/api/v2/\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        getByName("debug") {
            applicationIdSuffix = ".debug"
            isDebuggable = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
        //kotlinOptions.useIR = true
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = compose_version
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }
}

dependencies {
    //Functional Programming
    implementation(platform("io.arrow-kt:arrow-stack:_"))
    implementation("io.arrow-kt:arrow-core")
    implementation("io.arrow-kt:arrow-core-retrofit")

    //Android
    implementation(AndroidX.core.ktx)
    implementation(AndroidX.appCompat)
    implementation(Google.android.material)
    implementation(AndroidX.lifecycle.runtimeKtx)
    implementation(AndroidX.lifecycle.viewModelKtx)

    //Compose
    implementation(AndroidX.compose.ui)
    implementation(AndroidX.compose.foundation)
    implementation(AndroidX.compose.material)
    implementation(AndroidX.compose.ui.tooling)
    debugImplementation(AndroidX.compose.ui.tooling)
    implementation(AndroidX.compose.ui.toolingPreview)
    implementation(AndroidX.compose.material.icons.core)
    implementation(AndroidX.compose.material.icons.extended)
    implementation(AndroidX.compose.runtime.liveData)
    implementation(AndroidX.activity.compose)
    implementation(AndroidX.constraintLayout.compose)
    implementation(AndroidX.navigation.compose)

    //Accompanist for Compose
    implementation("com.google.accompanist:accompanist-permissions:_")
    implementation(Google.accompanist.pager)
    implementation(Google.accompanist.pager.indicators)

    //CameraX
    implementation(AndroidX.camera.camera2)
    implementation(AndroidX.camera.lifecycle)
    implementation(AndroidX.camera.view)

    //Image processing
    implementation(COIL)
    //Image processing for Compose
    implementation(COIL.compose)

    //Remote service
    implementation(Square.retrofit2.retrofit)
    implementation(Square.retrofit2.converter.moshi)
    implementation(Square.okHttp3.loggingInterceptor)
    implementation("se.ansman.kotshi:api:_")
    kapt("se.ansman.kotshi:compiler:_")

    //Local database
    kapt(AndroidX.room.compiler)
    implementation(AndroidX.room.runtime)
    implementation(AndroidX.room.ktx)

    //Dependencies Injections
    implementation(Koin.core)
    implementation(Koin.android)
    //Dependencies Injection for Compose
    implementation(Koin.compose)

    //Logging
    implementation(JakeWharton.timber)

    //JUnit 5
    testImplementation(Testing.junit.jupiter)
    testImplementation(Testing.junit.jupiter.api)
    testImplementation(Testing.junit.jupiter.engine)
    testImplementation(Testing.junit.jupiter.params)

    //Coroutine Testing
    testImplementation(KotlinX.coroutines.test)

    //Mock
    testImplementation(Testing.mockito.inline)
    testImplementation(Testing.mockito.core)
    //Mock for JUnit 5
    testImplementation(Testing.mockito.junitJupiter)

    //UI + Context based Testing
    androidTestImplementation(AndroidX.test.core)
    androidTestImplementation(AndroidX.test.runner)
    androidTestImplementation(AndroidX.test.rules)
    androidTestImplementation(AndroidX.archCore.testing)
    androidTestImplementation(KotlinX.coroutines.test)
}