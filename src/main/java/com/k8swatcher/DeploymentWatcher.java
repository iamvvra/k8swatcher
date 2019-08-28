package com.k8swatcher;

import javax.enterprise.context.Dependent;

import io.fabric8.kubernetes.api.model.apps.Deployment;

@Dependent
public class DeploymentWatcher extends ResourceWatcher<Deployment> {

    public DeploymentWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, Deployment resource) {
        super.eventReceived(action, resource);
    }

}