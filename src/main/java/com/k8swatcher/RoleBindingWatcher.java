package com.k8swatcher;

import javax.enterprise.context.Dependent;

import io.fabric8.kubernetes.api.model.rbac.RoleBinding;

@Dependent
public class RoleBindingWatcher extends ResourceWatcher<RoleBinding> {

    public RoleBindingWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, RoleBinding resource) {
        super.eventReceived(action, resource);
    }

}