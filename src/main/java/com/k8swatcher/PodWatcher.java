package com.k8swatcher;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import io.fabric8.kubernetes.api.model.Pod;

@Dependent
public class PodWatcher extends ResourceWatcher<Pod> {

    @Inject
    public PodWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, Pod resource) {
        super.eventReceived(action, resource);
    }

}