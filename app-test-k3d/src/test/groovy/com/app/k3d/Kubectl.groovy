package com.app.k3d

class Kubectl {

    private final String kubectlPath = "kubectl"
    private final String namespace
    private final boolean debug

    Kubectl(String namespace, boolean debug = false) {
        this.namespace = namespace
        this.debug = debug
    }

    String getPods() {
        def command = [kubectlPath, "-n", namespace, "get", "pods"]
        debug && println(command.join(" "))
        def stdOut = new StringBuilder()
        def stdErr = new StringBuilder()
        command.execute().waitForProcessOutput(stdOut, stdErr)
        debug && println(stdOut)
        debug && println(stdErr)
        stdOut.toString()
    }

    def showEvents() {
        def command = [kubectlPath, "-n", namespace, "get", "events"]
        println(command.join(" "))
        command.execute().waitForProcessOutput(System.out, System.err)
    }
}
