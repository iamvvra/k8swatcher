package com.k8swatcher;

import io.fabric8.kubernetes.api.model.ConfigMap;

public class ConfigMapWatcher extends ResourceWatcher<ConfigMap> {

    public ConfigMapWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, ConfigMap resource) {
        super.eventReceived(action, resource);
    }

}