package com.app.k3d

import java.nio.file.Files

import static java.nio.charset.StandardCharsets.UTF_8

class HelmInstaller {

    private static final File HELM_CHART_DIR = new File(Objects.requireNonNull(
            System.getProperty("test.helm.chart.dir"),
            "Must provide Helm chart directory path via system property."
    ))

    private final String chartName = "app"
    private final String helmPath = "helm"
    private final String namespace
    private final boolean debug

    HelmInstaller(String namespace, boolean debug = false) {
        this.namespace = namespace
        this.debug = debug
    }

    def install(List<String> valuesYaml, Map<String, String> files = [:]) {
        def valuesPathParameters = valuesYaml.collect { String it ->
            def valuesPath = Files.createTempFile("helm-values-", ".yaml")
            !debug && valuesPath.toFile().deleteOnExit()
            //noinspection GrDeprecatedAPIUsage // False positive
            Files.write(valuesPath, it.getBytes(UTF_8))
            return ["-f", "${valuesPath.toAbsolutePath()}"]
        }.flatten()

        def externalFiles = files.collect {
            def parameters = Files.createTempFile("helm-values-", ".yaml")
            !debug && parameters.toFile().deleteOnExit()
            //noinspection GrDeprecatedAPIUsage // False positive
            Files.write(parameters, it.value.getBytes(UTF_8))
            return "--set-file=${it.key}=${parameters.toAbsolutePath()}"
        }

        def command = [helmPath, "install", "app", "--create-namespace", "--namespace", namespace, "./$chartName"] \
                   + valuesPathParameters + externalFiles

        debug && println(command.join(" "))
        def stdOut = new StringBuilder()
        def stdErr = new StringBuilder()
        command.execute(null, HELM_CHART_DIR).waitForProcessOutput(stdOut, stdErr)
        debug && println(stdOut)
        debug && println(stdErr)
    }
}
