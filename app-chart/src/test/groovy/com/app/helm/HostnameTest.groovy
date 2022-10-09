package com.app.helm

import spock.lang.Specification

class HostnameTest extends Specification {

    def helm = new HelmTemplater()

    def "hostname can be customized"() {
        given:
        def defaultValuesYaml = """
        hostname: zed-app.com
        """

        when:
        def manifests = helm.template([defaultValuesYaml])

        then:
        def ingress = manifests.findIngress("app-ingress")
        (ingress.spec.rules as List<Object>)[0].host == "zed-app.com"
    }
}
