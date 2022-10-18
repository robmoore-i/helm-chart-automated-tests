## Automated Testing for Helm Charts

An example of a way to write a couple of types of automated tests for a Helm chart. 

## Requirements

This approach works for a wide range of versions, but here are the ones on my system.

```
$ helm version
version.BuildInfo{Version:"v3.9.2", GitCommit:"1addefbfe665c350f4daf868a9adc5600cc064fd", GitTreeState:"clean", GoVersion:"go1.18.4"}
```

```
$ k3d version
k3d version v5.4.6
k3s version v1.24.4-k3s1 (default)
```
