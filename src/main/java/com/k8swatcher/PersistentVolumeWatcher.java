package com.k8swatcher;

import javax.enterprise.context.Dependent;

import io.fabric8.kubernetes.api.model.PersistentVolume;

@Dependent
public class PersistentVolumeWatcher extends ResourceWatcher<PersistentVolume> {

    public PersistentVolumeWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, PersistentVolume resource) {
        super.eventReceived(action, resource);
    }

}