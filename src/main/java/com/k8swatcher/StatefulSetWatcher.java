package com.k8swatcher;

import io.fabric8.kubernetes.api.model.apps.StatefulSet;

public class StatefulSetWatcher extends ResourceWatcher<StatefulSet> {

    public StatefulSetWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, StatefulSet resource) {
        super.eventReceived(action, resource);
    }

}