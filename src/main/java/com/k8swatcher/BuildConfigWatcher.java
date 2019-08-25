package com.k8swatcher;

import io.fabric8.openshift.api.model.BuildConfig;

public class BuildConfigWatcher extends ResourceWatcher<BuildConfig> {

    public BuildConfigWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, BuildConfig resource) {
        super.eventReceived(action, resource);
    }

}