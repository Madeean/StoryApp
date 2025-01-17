import org.jetbrains.kotlin.konan.properties.Properties

plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
  id("com.google.devtools.ksp")
  id("kotlin-parcelize")
}

val localProperties = Properties()
project.rootProject.file("local.properties").inputStream().use { inputStream ->
  localProperties.load(inputStream)
}
val baseUrl = localProperties.getProperty("url") ?: "\"https://story-api.dicoding.dev/v1/\""

android {
  namespace = "com.madeean.storyapp2"
  compileSdk = 34

  defaultConfig {
    applicationId = "com.madeean.storyapp2"
    minSdk = 24
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    buildConfigField("String", "BASE_URL", baseUrl)
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
    buildConfig = true
  }
}

dependencies {
  implementation("com.jakewharton.timber:timber:5.0.1")

  implementation("androidx.core:core-ktx:1.9.0")
  implementation("androidx.appcompat:appcompat:1.6.1")
  implementation("com.google.android.material:material:1.8.0")
  implementation("androidx.constraintlayout:constraintlayout:2.1.4")
  testImplementation("junit:junit:4.13.2")
  androidTestImplementation("androidx.test.ext:junit:1.1.5")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

  implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
  implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")


  implementation("androidx.activity:activity-ktx:1.7.2")

  debugImplementation("com.github.chuckerteam.chucker:library:4.0.0")
//
  implementation("com.github.bumptech.glide:glide:4.16.0")
  implementation("com.squareup.retrofit2:retrofit:2.9.0")
  implementation("com.squareup.retrofit2:converter-gson:2.9.0")
  implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
//
  implementation("de.hdodenhof:circleimageview:3.1.0")
//
  implementation("androidx.datastore:datastore-preferences:1.0.0")
//
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2")

  implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
}