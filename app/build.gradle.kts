plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "np.com.hemnath.mylens"
    compileSdk = 33

    defaultConfig {
        applicationId = "np.com.hemnath.mylens"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    androidResources {
        noCompress += "tflite"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.10.1")
    implementation ("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.8.21")

    implementation ("androidx.viewpager2:viewpager2:1.0.0")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation ("com.google.mlkit:camera:16.0.0-beta3")
    implementation("com.google.android.material:material:1.9.0")

    implementation ("com.google.code.gson:gson:2.10.1")

    // Object detection feature with bundled default classifier
    implementation ("com.google.mlkit:object-detection:17.0.0")

    // Object detection feature with custom classifier support
    implementation ("com.google.mlkit:object-detection-custom:17.0.0")

    // Face features
    implementation ("com.google.mlkit:face-detection:16.1.5")

    // Pose detection with default models
    implementation ("com.google.mlkit:pose-detection:18.0.0-beta3")
    // Pose detection with accurate models
    implementation ("com.google.mlkit:pose-detection-accurate:18.0.0-beta3")

    implementation("androidx.core:core-splashscreen:1.0.1")

    implementation ("com.google.mlkit:image-labeling:17.0.7")


    // Text features
    implementation ("com.google.mlkit:text-recognition:16.0.0")

    // Face Mesh Detection
    implementation ("com.google.mlkit:face-mesh-detection:16.0.0-beta1")

    // CameraX
    val cameraxVersion = "1.2.3"
    implementation ("androidx.camera:camera-core:${cameraxVersion}")
    implementation ("androidx.camera:camera-camera2:${cameraxVersion}")
    implementation ("androidx.camera:camera-lifecycle:${cameraxVersion}")
    //implementation ("androidx.camera:camera-video:${cameraxVersion}")

    implementation ("androidx.camera:camera-view:${cameraxVersion}")
    //implementation ("androidx.camera:camera-extensions:${cameraxVersion}")

    //Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.7")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

}