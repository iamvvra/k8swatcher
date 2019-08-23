package com.k8swatcher;

import io.fabric8.kubernetes.api.model.Service;

public class ServiceWatcher extends ResourceWatcher<Service> {

    public ServiceWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, Service resource) {
        super.eventReceived(action, resource);
    }

}