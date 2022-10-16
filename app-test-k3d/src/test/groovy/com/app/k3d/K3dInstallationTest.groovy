package com.app.k3d

import spock.lang.Specification
import spock.util.concurrent.PollingConditions

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
        eventually(5, 5, 60) {
            assert kubectl.getPods().readLines().any {
                it.contains("app")
                it.contains("1/1")
                it.contains("Running")
            }
        }
    }

    boolean eventually(int initialDelaySeconds, int intervalSeconds, int timeoutSeconds, Closure<?> assertions) {
        def polling = new PollingConditions()
        polling.setInitialDelay(initialDelaySeconds)
        polling.setDelay(intervalSeconds)
        polling.setTimeout(timeoutSeconds)
        try {
            polling.eventually(assertions)
        } catch (e) {
            kubectl.showEvents()
            throw e
        }
        true
    }
}
