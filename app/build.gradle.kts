plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-parcelize")
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.bangkit.glowfyapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.bangkit.glowfyapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "BASE_URL", "\"https://glowfy-app-n3xvm6iu5q-et.a.run.app/\"")
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
        dataBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.annotation)

    // lifecycle
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //CardView
    implementation("androidx.cardview:cardview:1.0.0")

    //circle ImageView
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    //viewpager2
    implementation("androidx.viewpager2:viewpager2:1.1.0")

    //circle ImageView
    implementation("de.hdodenhof:circleimageview:3.1.0")

    //circle indicator
    implementation ("me.relex:circleindicator:2.1.6")

    // internet
    implementation ("com.squareup.retrofit2:retrofit:2.11.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.12.0")

    //Glide
    implementation ("com.github.bumptech.glide:glide:4.16.0")

    // camera x
    implementation ("androidx.camera:camera-core:1.3.3")
    implementation ("androidx.camera:camera-camera2:1.3.3")
    implementation ("androidx.camera:camera-lifecycle:1.3.3")
    implementation ("androidx.camera:camera-video:1.3.3")
    implementation ("androidx.camera:camera-view:1.3.3")
    implementation ("androidx.camera:camera-extensions:1.3.3")

    // datastore
    implementation ("androidx.datastore:datastore-preferences:1.1.1")

    //lottie
    implementation ("com.airbnb.android:lottie:6.4.0")

    //gif
    implementation ("pl.droidsonroids.gif:android-gif-drawable:1.2.28")

    // shimmer
    implementation ("com.facebook.shimmer:shimmer:0.5.0")

    // swipe refresh
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.2.0-alpha01")
}