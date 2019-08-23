package com.k8swatcher;

import io.fabric8.kubernetes.api.model.Node;

public class NodeWatcher extends ResourceWatcher<Node> {

    public NodeWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, Node resource) {
        super.eventReceived(action, resource);
    }

}