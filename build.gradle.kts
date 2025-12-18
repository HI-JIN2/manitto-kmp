plugins {
    kotlin("multiplatform") version "2.0.21"
    kotlin("plugin.serialization") version "2.0.21"
    id("org.jetbrains.compose") version "1.7.1"
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21"
}

group = "party.manitto"
version = "1.0.0"

kotlin {
    js(IR) {
        browser {
            commonWebpackConfig {
                outputFileName = "manitto.js"
            }
        }
        binaries.executable()
    }

    sourceSets {
        val jsMain by getting {
            dependencies {
                // Compose Multiplatform (Canvas-based, Android-like)
                implementation(compose.ui)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.runtime)
                
                // Ktor Client
                implementation("io.ktor:ktor-client-core:2.3.12")
                implementation("io.ktor:ktor-client-js:2.3.12")
                implementation("io.ktor:ktor-client-content-negotiation:2.3.12")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.12")
                
                // Kotlinx
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
            }
        }
    }
}

compose {
    web {
    }
}
