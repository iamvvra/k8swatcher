package com.k8swatcher;

import javax.enterprise.context.Dependent;

import io.fabric8.kubernetes.api.model.rbac.Role;

@Dependent
public class RoleWatcher extends ResourceWatcher<Role> {

    public RoleWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, Role resource) {
        super.eventReceived(action, resource);
    }

}