package com.k8swatcher;

import javax.enterprise.context.Dependent;

import io.fabric8.kubernetes.api.model.Endpoints;

@Dependent
public class EndpointsWatcher extends ResourceWatcher<Endpoints> {

    public EndpointsWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, Endpoints resource) {
        super.eventReceived(action, resource);
    }

}