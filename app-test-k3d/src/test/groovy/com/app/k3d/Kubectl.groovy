package com.app.k3d

class Kubectl {
    private final String namespace

    Kubectl(String namespace) {
        this.namespace = namespace
    }

    List<String> podNames() {
        []
    }
}
