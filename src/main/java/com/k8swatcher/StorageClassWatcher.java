package com.k8swatcher;

import io.fabric8.kubernetes.api.model.storage.StorageClass;

public class StorageClassWatcher extends ResourceWatcher<StorageClass> {

    public StorageClassWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, StorageClass resource) {
        super.eventReceived(action, resource);
    }

}