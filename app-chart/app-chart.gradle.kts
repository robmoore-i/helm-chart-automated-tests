@file:Suppress("UnstableApiUsage")

plugins {
    groovy
    `jvm-test-suite`
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
    jvmArgumentProviders += HelmChartDirProvider(layout.projectDirectory.dir("src/helm").asFile)
}

class HelmChartDirProvider(@InputDirectory @PathSensitive(PathSensitivity.RELATIVE) val helmChartDir: File) : CommandLineArgumentProvider {
    override fun asArguments(): List<String> = listOf("-Dtest.helm.chart.dir=${helmChartDir.absolutePath}")
}
