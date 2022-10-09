import java.io.FileOutputStream

tasks.register("helmTemplate") {
    val outputFileProvider = layout.buildDirectory.file("helmTemplate.yaml")
    doFirst {
        layout.buildDirectory.get().asFile.mkdirs()
        val outputFile = outputFileProvider.get().asFile
        logger.lifecycle("Rendering app's Helm Chart into file '${outputFile.absolutePath}'.")
        outputFile.createNewFile()
        project.exec {
            commandLine("helm", "template", file("src/helm/app").absolutePath)
            standardOutput = FileOutputStream(outputFile)
        }.assertNormalExitValue()
    }
}
