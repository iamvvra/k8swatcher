package com.k8swatcher;

import javax.enterprise.context.Dependent;

import io.fabric8.kubernetes.api.model.extensions.Ingress;

@Dependent
public class IngressWatcher extends ResourceWatcher<Ingress> {

    public IngressWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, Ingress resource) {
        super.eventReceived(action, resource);
    }

}