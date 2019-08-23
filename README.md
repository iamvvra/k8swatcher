
# k8swatcher
`k8swatcher` (`Kubernetes watcher`) watches different Kubernetes objects and notifies their state changes, abnormalities to developer collaboration tools such as Mattermost, Slack, Elasticsearch or alert an email. 
Technically, the`k8swatcher` can connect & watch almost all the flavors of Kubernetes versions - vannila kubernetes, `Redhat Openshift`, AWS `eks`, Google `gke`, Azure `aks`, DigitalOcean or the Platform9 - additionaly, it works in `minikube` and `minisihft`, too. It was tested in `minikube`, `minishift`, Redhat Openshift, eks & gke clusters.
The current version supports notifying events only to Mattermost.

## Setup
The `k8swatcher` can be run in different ways.
* A regular `Java` application
* A native binary
* `Docker` image
* Inside a `Kubernetes` cluster

### Build & run as a Java application
To build an Java uber jar, run

`$ mvn clean package`

This should produce an uber jar file in the target directory - `./target/k8swatcher-1.0-SNAPSHOT-runner.jar`. 

To run the watcher app, execute

`$ java -jar ./target/k8swatcher-1.0-SNAPSHOT-runner.jar`

To pass arguments overriding the values in the `application.properties` pass the property with the value along with the above command.

`$ java -jar ./target/k8swatcher-1.0-SNAPSHOT-runner.jar -Dquarkus.log.level=DEBUG`, this only accepts properties defined by the `k8swatcher` or the `quarkus` framework.

### Build & run as a native binary
The `k8swatcher` can be build as a native binary using GraalVM. For this GraalVM has to be installed and configured. Refer to GraalVM for installation and configuring.

Once the GraalVM is setup, run the below command to create a native binary,

`$ mvn clean package -Pnative`

This will generate a native binary file (based on the OS it was executed) in `./target/k8swatcher-1.0-SNAPSHOT-runner`.

This can be executed directly,

`$ ./target/k8swatcher-1.0-SNAPSHOT-runner`

Additional program arguments (application properties) can also be passed,

`$ ./target/k8swatcher-1.0-SNAPSHOT-runner -Dquarkus.log.level=DEBUG`

### Build & run as Docker image
Two ways to run a `k8swatcher` as a Docker image.
1. Build and run Java docker image
2. Build and run docker image with the native binary
#### Java docker image
The docker image creation has two steps - a) compile and build Java artifacts 2) create docker image from the generated `Java` binary artifact.

`$ mvn clean package` - should create, copy necessary artifacts and library jars to target directory.

Create the docker image

`$ docker build -t k8swatcher -f src/main/docker/Dockerfile.jvm .`

This will create a docker image from the `fabric8/java-alpine-openjdk8-jdk`, copying the `*runner.jar` and its libraries to the image.

Run the docker image

`$ docker run k8swatcher`

### Build, deploy and run inside a Kubernetes cluster
TODO

## Properties
Provide all necessary properties inside the `application.properties` for the `k8swatcher` to watch and notify the cluster events. 

Kubernetes monitoring properties
```properties
# cluster, master url
k8swatcher.k8s.master-url=https://master-cluster-url.cluster.com
# the access token used by the k8swatcher to authenticate the cluster
# Use this command to get the token, replace it with your ServiceAccount name
# echo (kubectl get secrets -o jsonpath="{.items[?(@.metadata.annotations['kubernetes\.io/service-account\.name']=='your-sa-account-name')].data.token}")|base64 -D
k8swatcher.k8s.oauth-token=
# trust self-signed certificates, in case your cluster has so, leave this to true
k8swatcher.k8s.trust-self-signed-cert=true
# this is unsupported in this version
k8swatcher.k8s.openshift=false
# set this to true your network requires a proxy to connect to the Kubernetes cluster, else false
k8swatcher.k8s.has-proxy=false
# if above is true, provide the proxy server host or the IP address, along with the port number, http://webproxy.yourcompany.com:8080
k8swatcher.k8s.http-proxy=
# if `has-proxy` is true, provide proxy server host or the IP address, along with the port number, https://webproxy.yourcompany.com:8080
k8swatcher.k8s.https-proxy=
# if your proxy requires authentication, provide the username
k8swatcher.k8s.proxy-username=
# if your proxy requires authentication, provide the password for the given username. Note, this is stored in plain text
k8swatcher.k8s.proxy-password=
```
Properties to customize what & how the Kubernetes resources are watched

```properties
# the namespaces to watch, names separated by comma
# you can also specify ALL (case sensitive) to watch all the namespaces, instead of listing all of them
k8swatcher.namespaces=default,kube-public,kube-system,istio,yournamespace
# Kubernetes resources that are to be watched. 
# All values should be in capital - all should be in singular. 
# Allowed objects are POD, SERVICE, PERSISTENTVOLUMECLAIM, SECRET, CONFIGMAP, JOB, 
# CRONJOB, HORIZONTALPODAUTOSCALER, BUILD, DEPLOYMENT, STATEFULSET, 
# INGRESS, NODE, NAMESPACE, PERSISTENTVOLUME, DAEMONSET, ROLE, ROLEBINDING, CLUSTERROLE, 
# CLUSTERROLEBINDING, REPLICATIONCONTROLLER, SERVICEACCOUNT, RESOURCEQUOTA, ALL, EVENT, REPLICASET, LIMITRANGE, ENDPOINT
# note, ALL will watch all objects
k8swatcher.resources=POD,SERVICE,INGRESS,NAMESPACE,ENDPOINTS,CONFIGMAP,SECRET,DEPLOYMENT
# the name you wish this cluster to be identified, ex., dev-cluster, us-east-eks-prod-cluster
k8swatcher.cluster-name=unnamed-cluster
```
Notification properties - the present version supports only Mattermost. The Mattermost should be reachable from the host the `k8swatcher` runs.
```properties
# enable to notify to Mattermost, if false, no events are notified
k8swatcher.mattermost-enabled=true
# the Mattermost host url
k8swatcher.mattermost-host=http://localhost:8065
# the personal access token of the Mattermost user - refer to Mattermost documentation.
k8swatcher.mattermost-api-token=oumr7gaicbgrjy1p9er1tbuc8h
# the user id the token is associated - this is not the regular user id used to signin the Mattermost
# use `mmctl` command to retrieve the user id
k8swatcher.mattermost-user-id=qeegisc1nbfyuqicaksmcjm61w
# the channel is to which the notifications are sent, the above user should belong to this channel. This is not the channel name you see in the Mattermost client window.
# use `mmctl` command to retrieve the channel id.
k8swatcher.mattermost-channel-id=h3t8gjwq1i85uq6zkj3dp1qgxr
# the display name, only works if override username is allowed, refer to the Mattermost docs.
k8swatcher.mattermost-user-display-name=k8swatcher
```

### TODO

 - [x] Developer integration tools
	 - [x] Mattermost
	 - [ ] Slack
	 - [ ] Elasticsearch
	 - [ ] Rocketchat
	 - [ ] Email
 - [x] Run as native binary
 - [x] Build and run docker image with Java
 - [x] Build and run docker image with native binary
 - [ ] Openshift specific objects (DeploymentConfig etc)
 - [ ] Filter type od events based on the resource
 - [ ] Kubernetes deployment doc
 - [ ] Helm
 - [ ] Bot to modify the `k8swatcher` configuration
 - [ ] Bot to query the watched kubernetes cluster
