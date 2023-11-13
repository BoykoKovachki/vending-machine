plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.vendingmachine"
    compileSdk = 32

    defaultConfig {
        applicationId = "com.example.vendingmachine"
        minSdk = 24
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"
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
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.4.2")
    implementation("com.google.android.material:material:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("android.arch.lifecycle:extensions:1.1.1")

    implementation("androidx.room:room-runtime:2.4.2")
    androidTestAnnotationProcessor("androidx.room:room-compiler:2.4.2")
    androidTestAnnotationProcessor("androidx.room:room-compiler-processing-testing:2.4.2")

    implementation("android.arch.persistence.room:runtime:1.1.1")
    annotationProcessor("android.arch.persistence.room:compiler:1.1.1")
    androidTestImplementation("android.arch.persistence.room:testing:1.1.1")

}