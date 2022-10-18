package com.app.helm

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory

import java.nio.file.Files

import static java.nio.charset.StandardCharsets.UTF_8

class HelmTemplater {

    private static final File HELM_CHART_DIR = new File(Objects.requireNonNull(
            System.getProperty("test.helm.chart.dir"),
            "Must provide Helm chart directory path via system property."
    ))

    private final String chartName = "app"
    private final String kubeVersion = "1.20.0"
    private final List<String> apiVersions = []
    private final String helmPath = "helm"
    private final boolean debug

    private ObjectMapper om = new ObjectMapper()
    private YAMLFactory yaml = new YAMLFactory()

    HelmTemplater(boolean debug = false) {
        this.debug = debug
    }

    Manifests template(List<String> valuesYaml) {
        def (manifests, err) = templateResult(valuesYaml)
        assert !err.contains("Error")
        return manifests
    }

    def templateResult(List<String> valuesYaml) {
        def valuesPathParameters = valuesYaml.collect { String it ->
            def valuesPath = Files.createTempFile("helm-values-", ".yaml")
            !debug && valuesPath.toFile().deleteOnExit()
            //noinspection GrDeprecatedAPIUsage // False positive
            Files.write(valuesPath, it.getBytes(UTF_8))
            return ["-f", "${valuesPath.toAbsolutePath()}"]
        }.flatten()

        def apiVersionParameters = apiVersions.collect { "--api-versions=$it" }


        def flags = ["--debug", "--dry-run", "--kube-version=$kubeVersion"]
        def command = [helmPath, "template", "app", "./$chartName"] + valuesPathParameters + apiVersionParameters + flags

        debug && println(command.join(" "))
        def stdOut = new StringBuilder()
        def stdErr = new StringBuilder()
        command.execute(null, HELM_CHART_DIR).waitForProcessOutput(stdOut, stdErr)
        debug && println(stdOut)
        debug && println(stdErr)

        def resultingYAML = yaml.createParser(stdOut.toString())
        def manifests = om.readValues(resultingYAML, new TypeReference<Map<String, Object>>() {}).readAll()

        [new Manifests(manifests), stdErr.toString()]
    }
}
