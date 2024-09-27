
object Dependencies {

    object Compose {
        const val version = "1.1.0"
        const val ui = "androidx.compose.ui:ui:$version"
        const val material = "androidx.compose.material:material:$version"
        const val tooling = "androidx.compose.ui:ui-tooling-preview:$version"
        const val livedata = "androidx.compose.runtime:runtime-livedata:$version"

        const val toolingTest = "androidx.compose.ui:ui-tooling:$version"
        const val uiTest = "androidx.compose.ui:ui-test-junit4:$version"

        const val accompanist = "com.google.accompanist:accompanist-systemuicontroller:0.18.0"
    }

    object Kotlin {
        const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2"
    }

    object Hilt {
        const val version = "2.40.5"
        const val android = "com.google.dagger:hilt-android:$version"
        const val navigation = "androidx.hilt:hilt-navigation-compose:1.0.0"
        const val compiler = "com.google.dagger:hilt-compiler:$version"
    }

    object Navigation {
        const val navigationCompose = "androidx.navigation:navigation-compose:2.5.0"
    }

    object Android {
        const val coreKtx = "androidx.core:core-ktx:1.6.0"
        const val appCompat = "androidx.appcompat:appcompat:1.3.1"
        const val material = "com.google.android.material:material:1.4.0"
    }

    object Lifecycle {
        const val lifecycleKtx = "androidx.lifecycle:lifecycle-runtime-ktx:2.5.1"
        const val viewModelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1"
        const val activityCompose = "androidx.activity:activity-compose:1.3.1"
    }
}