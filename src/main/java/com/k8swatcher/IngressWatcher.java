package com.k8swatcher;

import io.fabric8.kubernetes.api.model.extensions.Ingress;

public class IngressWatcher extends ResourceWatcher<Ingress> {

    public IngressWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, Ingress resource) {
        super.eventReceived(action, resource);
    }

}