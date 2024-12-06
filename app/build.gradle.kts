plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.ptdd_btl_qlct_n7_final2"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.ptdd_btl_qlct_n7_final2"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    //Lombok
    compileOnly (libs.lombok)
    annotationProcessor (libs.lombok)

    //RoomDB
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)
}