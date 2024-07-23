plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-parcelize")
}

android {
    namespace = "com.kkjang.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {

    implementation(project(":domain"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // SqlLite
    implementation(libs.sqlite)
    implementation(libs.sqlite.cipher)

    // Hilt
    implementation(libs.google.hilt)
    implementation(libs.google.hilt.compiler)

    // Network
    implementation(libs.okhttp)
    implementation(libs.okhttp.urlconnection)
    implementation(libs.okhttp.interceptor)
    implementation(libs.retrofit)
    implementation(libs.gson)

    // Retrofit
    implementation(libs.retrofit.scalars)
    implementation(libs.retrofit.simplexml)
    implementation(libs.retrofit.gson)
    implementation(libs.retrofit.adapter.rxjava2)
    implementation(libs.retrofit.jaxb)

}