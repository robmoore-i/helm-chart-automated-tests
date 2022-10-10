package com.app.k3d

class Kubectl {

    private final String kubectlPath = "kubectl"
    private final String namespace
    private final boolean debug

    Kubectl(String namespace, boolean debug = false) {
        this.namespace = namespace
        this.debug = debug
    }

    List<String> podNames() {
        def command = [kubectlPath, "-n", namespace, "get", "pods"]
        debug && println(command.join(" "))
        def stdOut = new StringBuilder()
        def stdErr = new StringBuilder()
        command.execute().waitForProcessOutput(stdOut, stdErr)
        debug && println(stdOut)
        debug && println(stdErr)
        []
    }
}
