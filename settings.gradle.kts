include("app-chart")

fun renameBuildFiles(project: ProjectDescriptor) {
    project.buildFileName = if (File(
            project.projectDir,
            "${project.name}.gradle.kts"
        ).isFile
    ) "${project.name}.gradle.kts" else "${project.name}.gradle"
    project.children.forEach {
        renameBuildFiles(it)
    }
}

renameBuildFiles(rootProject)
