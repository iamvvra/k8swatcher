package com.k8swatcher;

import javax.enterprise.context.Dependent;

import io.fabric8.kubernetes.api.model.Service;

@Dependent
public class ServiceWatcher extends ResourceWatcher<Service> {

    public ServiceWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, Service resource) {
        super.eventReceived(action, resource);
    }

}