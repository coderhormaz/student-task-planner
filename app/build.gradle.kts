plugins {
    id("com.android.application")
}

android {
    namespace = "com.hormaz.studentplanner"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.hormaz.studentplanner"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Supabase configuration
        buildConfigField("String", "SUPABASE_URL", "\"SUPABASE_URL_PLACEHOLDER\"")
        buildConfigField("String", "SUPABASE_ANON_KEY", "\"SUPABASE_ANON_KEY_PLACEHOLDER\"")
    }

    signingConfigs {
        create("release") {
            // Use debug keystore for direct installation
            storeFile = file(System.getProperty("user.home") + "/.android/debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
}

dependencies {
    // ── Core AndroidX ──
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.core:core:1.15.0")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.recyclerview:recyclerview:1.4.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.viewpager2:viewpager2:1.1.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.8.7")
    implementation("androidx.lifecycle:lifecycle-livedata:2.8.7")
    implementation("androidx.lifecycle:lifecycle-runtime:2.8.7")

    // ── Material Design 3 ──
    implementation("com.google.android.material:material:1.13.0")

    // ── Room (local DB) ──
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")

    // ── Networking (Supabase REST via Retrofit) ──
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.google.code.gson:gson:2.11.0")

    // ── Calendar View ──
    implementation("com.applandeo:material-calendar-view:1.9.2")

    // ── Charts (Stats Dashboard) ──
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // ── Image Loading ──
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    // ── Lottie Animations ──
    implementation("com.airbnb.android:lottie:6.7.1")

    // ── Google Sign-In ──
    implementation("com.google.android.gms:play-services-auth:21.3.0")

    // ── Testing ──
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}
