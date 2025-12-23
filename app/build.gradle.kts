plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  kotlin("plugin.serialization") version "2.1.21"
  alias(libs.plugins.google.ksp) // ksp
  id("kotlin-kapt") // brv 必须引入此插件
}

android {
  signingConfigs {
    getByName("debug") {
      storeFile = file("D:\\jdy2002\\appkey\\jdy.jks")
      storePassword = "jdy200255"
      keyAlias = "jdy2002"
      keyPassword = "jdy200255"
    }
  }
  namespace = "xyz.jdynb.tv"
  compileSdk = 36

  defaultConfig {
    applicationId = "xyz.jdynb.tv"
    minSdk = 21
    targetSdk = 36
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildFeatures {
    dataBinding = true
    viewBinding = true
    buildConfig = true
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
  kotlinOptions {
    jvmTarget = "11"
  }
}

dependencies {

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.material)
  implementation(libs.androidx.activity)
  implementation(libs.androidx.constraintlayout)
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
}