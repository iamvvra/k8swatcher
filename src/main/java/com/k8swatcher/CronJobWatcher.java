package com.k8swatcher;

import io.fabric8.kubernetes.api.model.batch.CronJob;

public class CronJobWatcher extends ResourceWatcher<CronJob> {

    public CronJobWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, CronJob resource) {
        super.eventReceived(action, resource);
    }

}