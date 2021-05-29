plugins {
    kotlin("multiplatform") version "1.5.20-M1" apply false
    id("org.jetbrains.kotlin.plugin.lombok") version "1.5.20-M1" apply false
}

buildscript {
    repositories {
        mavenCentral()
    }
}

allprojects {
    repositories {
        mavenCentral()
    }
}