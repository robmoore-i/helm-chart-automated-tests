package com.app.k3d

import spock.lang.Specification

class K3dInstallationTest extends Specification {

    private static final String NAMESPACE = "test-namespace"

    def helm = new HelmInstaller(NAMESPACE, true)
    def kubectl = new Kubectl(NAMESPACE, true)

    def "can install into k3d cluster"() {
        given:
        def defaultValuesYaml = """
        hostname: app-k3d.127.0.0.1.nip.io
        """

        when:
        helm.install([defaultValuesYaml])

        then:
        kubectl.podNames().contains("app")
    }
}
