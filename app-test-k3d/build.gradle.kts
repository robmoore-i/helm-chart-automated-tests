@file:Suppress("UnstableApiUsage")

import com.appbuild.LazyDirectorySystemPropertyProvider

plugins {
    groovy
    `jvm-test-suite`
    id("appbuild.directory-provider")
}

description = "Tests that The App's Helm Chart can be installed in a k3d cluster"

repositories {
    mavenCentral()
}

@Suppress("HasPlatformType")
val helmChart by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
    attributes.attribute(Usage.USAGE_ATTRIBUTE, objects.named("helm-chart"))
}

dependencies {
    helmChart(project(":app-chart"))
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

abstract class K3dTask @Inject constructor(private val exec: ExecOperations) : DefaultTask() {
    @get:Input
    abstract val commands: ListProperty<String>

    @TaskAction
    fun runK3dCommand() {
        exec.exec {
            val command = mutableListOf("k3d") + commands.get()
            commandLine(command)
        }
    }
}

val k3dCreate by tasks.registering(K3dTask::class) {
    commands.set(listOf("cluster", "create", "test-cluster"))
}

val k3dRm by tasks.registering(K3dTask::class) {
    commands.set(listOf("cluster", "rm", "test-cluster"))
}

testing {
    suites {
        named<JvmTestSuite>("test") {
            useSpock()
        }
    }
}

tasks.named<Test>("test") {
    testLogging.showStandardStreams = true
    inputs.files(helmChart)
    dependsOn(k3dCreate)
    finalizedBy(k3dRm)
    jvmArgumentProviders += LazyDirectorySystemPropertyProvider("test.helm.chart.dir", providers.provider { helmChart.singleFile.parentFile })
}
