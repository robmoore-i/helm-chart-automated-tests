package com.app.k3d

import spock.lang.Specification

class K3dInstallationTest extends Specification {

    private static final String NAMESPACE = "test-namespace"

    def helm = new HelmInstaller(NAMESPACE)
    def kubectl = new Kubectl(NAMESPACE)

    def "can install into k3d cluster"() {
        given:
        def defaultValuesYaml = """
        hostname: zed-app.com
        """

        when:
        helm.install([defaultValuesYaml])

        then:
        kubectl.podNames().contains("app")
    }
}
