package com.k8swatcher;

import javax.enterprise.context.Dependent;

import io.fabric8.kubernetes.api.model.ResourceQuota;

@Dependent
public class ResourceQuotaWatcher extends ResourceWatcher<ResourceQuota> {

    public ResourceQuotaWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, ResourceQuota resource) {
        super.eventReceived(action, resource);
    }

}