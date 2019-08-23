package com.k8swatcher;

import io.fabric8.kubernetes.api.model.LimitRange;

public class LimitRangeWatcher extends ResourceWatcher<LimitRange> {

    public LimitRangeWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, LimitRange resource) {
        super.eventReceived(action, resource);
    }

}