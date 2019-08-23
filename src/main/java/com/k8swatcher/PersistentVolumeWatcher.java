package com.k8swatcher;

import io.fabric8.kubernetes.api.model.PersistentVolume;

public class PersistentVolumeWatcher extends ResourceWatcher<PersistentVolume> {

    public PersistentVolumeWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, PersistentVolume resource) {
        super.eventReceived(action, resource);
    }

}