package com.k8swatcher;

import io.fabric8.kubernetes.api.model.apps.Deployment;

public class DeploymentWatcher extends ResourceWatcher<Deployment> {

    public DeploymentWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, Deployment resource) {
        super.eventReceived(action, resource);
    }

}