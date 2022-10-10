package com.appbuild

import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.process.CommandLineArgumentProvider
import java.io.File

class DirectorySystemPropertyProvider(
    @Input val key: String, @InputDirectory @PathSensitive(PathSensitivity.RELATIVE) val dir: File
) : CommandLineArgumentProvider {
    override fun asArguments(): List<String> = listOf("-D${key}=${dir.absolutePath}")
}

class LazyDirectorySystemPropertyProvider(
    @Input val key: String, @InputDirectory @PathSensitive(PathSensitivity.RELATIVE) val dir: Provider<File>
) : CommandLineArgumentProvider {
    override fun asArguments(): List<String> = listOf("-D${key}=${dir.get().absolutePath}")
}