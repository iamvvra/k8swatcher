package com.k8swatcher;

import io.fabric8.kubernetes.api.model.rbac.RoleBinding;

public class RoleBindingWatcher extends ResourceWatcher<RoleBinding> {

    public RoleBindingWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, RoleBinding resource) {
        super.eventReceived(action, resource);
    }

}