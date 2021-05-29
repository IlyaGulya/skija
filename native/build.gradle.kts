plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }

        withJava()
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
//                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:$coroutinesVersion")
//                compileOnly(lombok)
//                compileOnly(jetbrainsAnnotations)
            }

        }
        val jvmTest by getting {
            dependencies {
//                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
                implementation(kotlin("test-junit"))
            }
        }
    }

}