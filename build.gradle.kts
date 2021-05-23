buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        // Should be fixed in Kotlin 1.5.10 - see: See https://youtrack.jetbrains.com/issue/KT-41142
        //noinspection DifferentKotlinGradleVersion
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.0")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.5.0")
    }
}

repositories {
    mavenCentral()
}

plugins {
    `kotlin-dsl`
    kotlin("plugin.serialization") version "1.5.0"
    `java-gradle-plugin`
    `maven-publish`
}

group = "com.github.vaughandroid"
version = "0.1.5-SNAPSHOT"

gradlePlugin {
    plugins {
        create("scrimp") {
            id = "scrimp"
            implementationClass = "me.vaughandroid.gradle.scrimp.ScrimpPlugin"
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.1")

    testImplementation("junit:junit:4.12")
    testImplementation("com.google.truth:truth:1.0")
}

tasks {
    test {
        testLogging {
            // Can use this to log stdout (including println()) from tests.
            // TODO: Set up a build flag for this.
            showStandardStreams = true
        }
    }
}
