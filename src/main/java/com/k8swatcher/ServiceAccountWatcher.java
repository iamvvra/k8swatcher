package com.k8swatcher;

import javax.enterprise.context.Dependent;

import io.fabric8.kubernetes.api.model.ServiceAccount;

@Dependent
public class ServiceAccountWatcher extends ResourceWatcher<ServiceAccount> {

    public ServiceAccountWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, ServiceAccount resource) {
        super.eventReceived(action, resource);
    }

}