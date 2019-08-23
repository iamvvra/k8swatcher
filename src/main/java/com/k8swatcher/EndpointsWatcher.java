package com.k8swatcher;

import io.fabric8.kubernetes.api.model.Endpoints;

public class EndpointsWatcher extends ResourceWatcher<Endpoints> {

    public EndpointsWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, Endpoints resource) {
        super.eventReceived(action, resource);
    }

}