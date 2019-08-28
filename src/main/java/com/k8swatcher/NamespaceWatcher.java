package com.k8swatcher;

import javax.enterprise.context.Dependent;

import io.fabric8.kubernetes.api.model.Namespace;

@Dependent
public class NamespaceWatcher extends ResourceWatcher<Namespace> {

    public NamespaceWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, Namespace resource) {
        super.eventReceived(action, resource);
    }

}