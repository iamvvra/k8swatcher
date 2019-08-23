package com.k8swatcher;

import io.fabric8.kubernetes.api.model.Secret;

public class SecretWatcher extends ResourceWatcher<Secret> {

    public SecretWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, Secret resource) {
        super.eventReceived(action, resource);
    }

}