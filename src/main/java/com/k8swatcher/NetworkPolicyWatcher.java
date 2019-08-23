package com.k8swatcher;

import io.fabric8.kubernetes.api.model.networking.NetworkPolicy;

public class NetworkPolicyWatcher extends ResourceWatcher<NetworkPolicy> {

    public NetworkPolicyWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, NetworkPolicy resource) {
        super.eventReceived(action, resource);
    }

}