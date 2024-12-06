plugins {
    id("com.android.application")  // Android application plugin
    id("org.jetbrains.kotlin.android")  // Kotlin plugin
    id("org.jetbrains.kotlin.plugin.compose")  // Kotlin Compose plugin
    id("com.google.gms.google-services")  // Firebase plugin
}


android {
    namespace = "com.example.bloodbankmerafinal"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.bloodbankmerafinal"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    // Android Core Libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose Libraries
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Android Support Libraries
    implementation(libs.androidx.appcompat)

    // Firebase Libraries
    implementation(platform("com.google.firebase:firebase-bom:33.6.0")) // Firebase BOM for versions management
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth:21.2.0")
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.android.gms:play-services-auth:21.2.0")

    // UI Components
    implementation("androidx.recyclerview:recyclerview:1.3.0") // Correct RecyclerView Dependency
    implementation("androidx.viewpager2:viewpager2:1.0.0") // ViewPager2 Dependency
    implementation("com.google.android.material:material:1.9.0") // Material Design Components
    implementation("androidx.cardview:cardview:1.0.0") // CardView Dependency

    // Testing Libraries
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
