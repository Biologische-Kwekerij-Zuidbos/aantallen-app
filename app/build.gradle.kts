/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java application project to get you started.
 * For more details on building Java & JVM projects, please refer to https://docs.gradle.org/8.6/userguide/building_java_projects.html in the Gradle documentation.
 */

plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application

    // Apply GraalVM Native Image plugin
    id("org.graalvm.buildtools.native") version "0.10.1"
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("com.diffplug.spotless") version "6.25.0"
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use JUnit test framework.
    testImplementation(libs.junit)

    // This dependency is used by the application.
    implementation(libs.guava)

    // For reading and writing Excel files (*.xls, *.xlsx)
    // https://mvnrepository.com/artifact/org.apache.poi/poi
    implementation("org.apache.poi:poi:5.2.5")
    // https://mvnrepository.com/artifact/org.apache.poi/poi-ooxml
    implementation("org.apache.poi:poi-ooxml:5.2.5")

    // Logging
    // https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-core
    implementation(platform("org.apache.logging.log4j:log4j-bom:2.23.0"))
    implementation("org.apache.logging.log4j:log4j-core")
    implementation("org.apache.logging.log4j:log4j-api")
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    // Define the main class for the application.
    mainClass = "org.example.App"
}

graalvmNative {
    toolchainDetection.set(true)
}

javafx {
    version = "21.0.2"
    modules = listOf("javafx.controls")
}

spotless {
    // optional: limit format enforcement to just the files changed by this feature branch
    // ratchetFrom("origin/main")

    format("misc") {
        // define the files to apply `misc` to
        target("*.gradle", ".gitattributes", ".gitignore")

        // define the steps to apply to those files
        trimTrailingWhitespace()
        indentWithTabs() // or spaces. Takes an integer argument if you don't like 4
        endWithNewline()
    }

    java {
        // don't need to set target, it is inferred from java

        // apply a specific flavor of google-java-format
        googleJavaFormat("1.19.2").aosp().reflowLongStrings().skipJavadocFormatting()
        // fix formatting of type annotations
        formatAnnotations()
        // make sure every file has the following copyright header.
        // optionally, Spotless can set copyright years by digging
        // through git history (see "license" section below)
        licenseHeader("/* (C)\$YEAR */")
    }
}