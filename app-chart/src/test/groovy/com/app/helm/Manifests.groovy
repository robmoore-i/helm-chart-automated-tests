package com.app.helm

class Manifests {
    List<Map<String, Object>> manifests

    Manifests(List<Map<String, Object>> manifests) {
        this.manifests = manifests
    }

    List<Map<String, Object>> all() {
        return this.manifests
    }

    Map findOneBy(Map args) {
        return manifests.find {
            it.apiVersion == args.apiVersion &&
                    it.kind == args.kind &&
                    it.metadata.name == args.name
        }
    }

    Map findIngress(String name) {
        return findOneBy(apiVersion: "networking.k8s.io/v1", kind: "Ingress", name: name)
    }
}

