package com.k8swatcher;

import io.fabric8.kubernetes.api.model.ResourceQuota;

public class ResourceQuotaWatcher extends ResourceWatcher<ResourceQuota> {

    public ResourceQuotaWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, ResourceQuota resource) {
        super.eventReceived(action, resource);
    }

}