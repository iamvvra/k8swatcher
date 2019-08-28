package com.k8swatcher;

import javax.enterprise.context.Dependent;

import io.fabric8.kubernetes.api.model.apps.DaemonSet;

@Dependent
public class DaemonSetWatcher extends ResourceWatcher<DaemonSet> {

    public DaemonSetWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, DaemonSet resource) {
        super.eventReceived(action, resource);
    }

}