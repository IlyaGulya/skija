plugins {
    kotlin("multiplatform")
    id("org.jetbrains.kotlin.plugin.lombok")
}

val lombok by configurations.creating
val jetbrainsAnnotations by configurations.creating
dependencies {
    lombok("org.projectlombok:lombok:1.18.16")
    jetbrainsAnnotations("org.jetbrains:annotations:19.0.0")
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
            kotlin.srcDirs("src/main/java")
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
//                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:$coroutinesVersion")
                compileOnly(lombok)
                compileOnly(jetbrainsAnnotations)
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