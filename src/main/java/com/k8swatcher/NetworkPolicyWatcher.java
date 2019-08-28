package com.k8swatcher;

import javax.enterprise.context.Dependent;

import io.fabric8.kubernetes.api.model.networking.NetworkPolicy;

@Dependent
public class NetworkPolicyWatcher extends ResourceWatcher<NetworkPolicy> {

    public NetworkPolicyWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, NetworkPolicy resource) {
        super.eventReceived(action, resource);
    }

}