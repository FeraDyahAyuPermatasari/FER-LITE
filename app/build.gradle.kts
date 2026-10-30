plugins {
    alias(libs.plugins.android.application)
    // ===== [FITUR BARU 1 - ROOM] KSP UNTUK MEMBUAT KODE DATABASE OTOMATIS =====
    id("com.google.devtools.ksp") version "2.3.10"
    // ===== [AKHIR FITUR BARU 1] =====
}

android {
    namespace = "com.example.fera_lite"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.feradyahayupermatasari"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // ===== [FITUR BARU 1 - ROOM] DEPENDENSI ROOM 2.8.5 =====
    implementation("androidx.room:room-runtime:2.8.5")
    ksp("androidx.room:room-compiler:2.8.5")
    // ===== [AKHIR FITUR BARU 1] =====
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}
