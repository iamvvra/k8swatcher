package com.k8swatcher;

import java.util.Optional;
import java.util.stream.Stream;

public enum Resource {
    POD, SERVICE, PERSISTENTVOLUMECLAIM, SECRET, CONFIGMAP, JOB, CRONJOB, HORIZONTALPODAUTOSCALER, BUILD,
    DEPLOYMENTCONFIG, DEPLOYMENT, STATEFULSET, INGRESS, NODE, NAMESPACE, PERSISTENTVOLUME, DAEMONSET, ROLE, ROLEBINDING,
    CLUSTERROLE, CLUSTERROLEBINDING, REPLICATIONCONTROLLER, SERVICEACCOUNT, RESOURCEQUOTAS, ALL, EVENT, REPLICASET,
    LIMITRANGE, ENDPOINTS;

    public static Optional<Resource> valueof(String name) {
        return Stream.of(Resource.values()).filter(s -> s.name().equals(name.toUpperCase())).findFirst();
    }
}