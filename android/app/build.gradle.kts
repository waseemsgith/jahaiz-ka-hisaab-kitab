import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlinKapt)
    alias(libs.plugins.hiltAndroid)
}

fun localApiBase(): String {
    val f = rootProject.file("local.properties")
    if (!f.exists()) return ""
    val p = Properties()
    f.inputStream().use { p.load(it) }
    return p.getProperty("API_BASE_URL")?.trim().orEmpty()
}

android {
    namespace = "com.waseemsgith.jahaiz"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.waseemsgith.jahaiz"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        val baseProp = localApiBase().ifBlank { project.findProperty("API_BASE_URL")?.toString() ?: "" }.ifBlank { "https://jahaiz-backend.onrender.com/" }
        val base = if (!baseProp.endsWith("/")) "$baseProp/" else baseProp
        buildConfigField("String", "BASE_URL", "\"${base}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
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
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1,LICENSE.md,LICENSE.txt}"
        }
    }
}

dependencies {
    implementation(libs.coreKtx)
    implementation(libs.lifecycleRuntimeKtx)
    implementation(libs.activityCompose)

    implementation(platform(libs.composeBom))
    implementation(libs.composeUi)
    implementation(libs.composeUiGraphics)
    implementation(libs.composeUiToolingPreview)
    implementation(libs.composeMaterial3)
    implementation(libs.composeMaterialIcons)

    implementation(libs.navigationCompose)
    implementation(libs.lifecycleViewmodelCompose)

    implementation(libs.hiltAndroid)
    kapt(libs.hiltCompiler)
    implementation(libs.hiltNavigationCompose)

    implementation(libs.retrofit)
    implementation(libs.retrofitGson)
    implementation(libs.okhttp)
    implementation(libs.okhttpLogging)

    implementation(libs.lottieCompose)
    implementation(libs.coilCompose)

    implementation(libs.coroutinesAndroid)

    implementation(libs.datastore)

    implementation(libs.itextCore)

    implementation(libs.accompanistPermissions)
    implementation(libs.accompanistSystemuicontroller)

    debugImplementation(libs.composeUiTooling)
}

kapt {
    correctErrorTypes = true
}
