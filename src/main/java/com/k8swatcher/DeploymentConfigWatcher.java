package com.k8swatcher;

import javax.enterprise.context.Dependent;

import io.fabric8.openshift.api.model.DeploymentConfig;

@Dependent
public class DeploymentConfigWatcher extends ResourceWatcher<DeploymentConfig> {

    public DeploymentConfigWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, DeploymentConfig resource) {
        super.eventReceived(action, resource);
    }

}