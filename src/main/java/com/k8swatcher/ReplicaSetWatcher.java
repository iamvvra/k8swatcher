package com.k8swatcher;

import io.fabric8.kubernetes.api.model.apps.ReplicaSet;

public class ReplicaSetWatcher extends ResourceWatcher<ReplicaSet> {

    public ReplicaSetWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, ReplicaSet resource) {
        super.eventReceived(action, resource);
    }

}