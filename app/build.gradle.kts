plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.0"
}

android {
    namespace = "com.friends.ggiriggiri"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.friends.ggiriggiri"
        minSdk = 25
        targetSdk = 35
        versionCode = 24
        versionName = "1.24.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file("keystore.jks") // Keystore 파일 경로
            storePassword = System.getenv("KEYSTORE_PASSWORD") ?: "990602"
            keyAlias = System.getenv("KEY_ALIAS") ?: "my-key-alias"
            keyPassword = System.getenv("KEY_PASSWORD") ?: "990602"
        }
    }

    buildTypes {
        release {
            // 코드 최적화 및 난독화 활성화
            isMinifyEnabled = true

            // 리소스 최적화 (안 쓰는 drawable/string 제거)
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release") // 서명 설정 추가
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.13"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.13.0"))


    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")

    //파이어스토어
    implementation("com.google.firebase:firebase-firestore-ktx")

    //파이어베이스 스토리지
    implementation("com.google.firebase:firebase-storage")

    //파이어베이스 functions
    implementation("com.google.firebase:firebase-functions-ktx")

    //fcm
    implementation ("com.google.firebase:firebase-messaging:23.4.1")
    implementation ("com.squareup.okhttp3:okhttp:4.12.0")

    //스와이프로해서 리프래쉬쓰려고
    implementation(libs.androidx.material)

    // 캐러셀
    implementation(libs.androidx.foundation)

    // coil-compose
    implementation(libs.coil.compose)

    //컴포즈 ui툴
    debugImplementation ("androidx.compose.ui:ui-tooling:1.2.0-rc01")
    debugImplementation ("androidx.customview:customview:1.1.0") // being pulled in by another dependency
    debugImplementation ("androidx.customview:customview:1.2.0-alpha01")



    implementation("androidx.compose.material:material-icons-extended:1.7.6")
    implementation("androidx.navigation:navigation-compose:2.8.5")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation ("com.google.accompanist:accompanist-pager:0.30.1")


    // shimmer 효과
    implementation(libs.compose.shimmer)

    // 직렬화
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")


    // Glide
    implementation(libs.glide)
    annotationProcessor(libs.compiler) // 버전 맞추기!
    implementation("com.github.penfeizhou.android.animation:apng:2.24.0")

    // 카카오
    implementation("com.kakao.sdk:v2-all:2.20.6") // 전체 모듈 설치, 2.11.0 버전부터 지원
    implementation("com.kakao.sdk:v2-user:2.20.6") // 카카오 로그인 API 모듈
    implementation("com.kakao.sdk:v2-share:2.20.6") // 카카오톡 공유 API 모듈
    implementation("com.kakao.sdk:v2-talk:2.20.6") // 카카오톡 채널, 카카오톡 소셜, 카카오톡 메시지 API 모듈
    implementation("com.kakao.sdk:v2-friend:2.20.6") // 피커 API 모듈
    implementation("com.kakao.sdk:v2-navi:2.20.6") // 카카오내비 API 모듈
    implementation("com.kakao.sdk:v2-cert:2.20.6") // 카카오톡 인증 서비스 API 모듈

    //네이버
    implementation(files("libs/oauth-5.10.0.aar"))
    implementation("com.airbnb.android:lottie:3.1.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("androidx.legacy:legacy-support-core-utils:1.0.0")
    implementation("androidx.browser:browser:1.4.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("androidx.security:security-crypto:1.1.0-alpha06")
    implementation("androidx.core:core-ktx:1.3.0")
    implementation("androidx.fragment:fragment-ktx:1.3.6")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.2.1")


    // Google Play services
    implementation ("com.google.gms:google-services:4.3.15")
    implementation ("com.google.firebase:firebase-auth:22.0.0")
    implementation ("com.google.firebase:firebase-bom:32.0.0")
    implementation ("com.google.android.gms:play-services-auth:20.5.0")

    implementation ("androidx.work:work-runtime-ktx:2.8.1")

    implementation ("androidx.core:core-splashscreen:1.0.1")

    //room
    implementation("androidx.room:room-runtime:2.7.1")
    kapt("androidx.room:room-compiler:2.7.1")


}
kapt {
    correctErrorTypes = true
}