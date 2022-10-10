package com.app.k3d

class HelmInstaller {

    private static final File HELM_CHART_DIR = new File(Objects.requireNonNull(
            System.getProperty("test.helm.chart.dir"),
            "Must provide Helm chart directory path via system property."
    ))

    private final String namespace

    HelmInstaller(String namespace) {
        this.namespace = namespace
    }

    def install(List<String> valuesYaml) {
        println(HELM_CHART_DIR)
    }
}
