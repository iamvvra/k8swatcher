package com.k8swatcher;

import io.fabric8.kubernetes.api.model.ReplicationController;

public class ReplicationControllerWatcher extends ResourceWatcher<ReplicationController> {

    public ReplicationControllerWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, ReplicationController resource) {
        super.eventReceived(action, resource);
    }

}