package com.k8swatcher;

import io.fabric8.kubernetes.api.model.PersistentVolumeClaim;

public class PersistentVolumeClaimWatcher extends ResourceWatcher<PersistentVolumeClaim> {

    public PersistentVolumeClaimWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, PersistentVolumeClaim resource) {
        super.eventReceived(action, resource);
    }

}