@file:Suppress("UnstableApiUsage")

import com.appbuild.DirectorySystemPropertyProvider

plugins {
    groovy
    `jvm-test-suite`
    id("appbuild.directory-provider")
}

description = "A Helm chart which install The App"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

testing {
    suites {
        named<JvmTestSuite>("test") {
            useSpock()

            dependencies {
                implementation("com.fasterxml.jackson.core:jackson-databind:2.13.4")
                implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.13.4")
            }
        }
    }
}

tasks.named<Test>("test") {
    testLogging.showStandardStreams = true
    jvmArgumentProviders += DirectorySystemPropertyProvider("test.helm.chart.dir", layout.projectDirectory.dir("src/helm").asFile)
}

configurations.create("helmChart") {
    isCanBeConsumed = true
    isCanBeResolved = false
    outgoing.artifact(layout.projectDirectory.dir("src/helm/app"))
    attributes.attribute(Usage.USAGE_ATTRIBUTE, objects.named("helm-chart"))
}
