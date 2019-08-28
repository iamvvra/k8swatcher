package com.k8swatcher;

import javax.enterprise.context.Dependent;

import io.fabric8.kubernetes.api.model.batch.Job;

@Dependent
public class JobWatcher extends ResourceWatcher<Job> {

    public JobWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, Job resource) {
        super.eventReceived(action, resource);
    }

}