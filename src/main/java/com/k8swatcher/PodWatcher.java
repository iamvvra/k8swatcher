package com.k8swatcher;

import io.fabric8.kubernetes.api.model.Pod;

public class PodWatcher extends ResourceWatcher<Pod> {

    public PodWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, Pod resource) {
        super.eventReceived(action, resource);
    }

}