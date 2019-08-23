package com.k8swatcher;

import io.fabric8.openshift.api.model.Build;

public class BuildWatcher extends ResourceWatcher<Build> {

    public BuildWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, Build resource) {
        super.eventReceived(action, resource);
    }

}