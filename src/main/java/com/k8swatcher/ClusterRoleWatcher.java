package com.k8swatcher;

import javax.enterprise.context.Dependent;

import io.fabric8.kubernetes.api.model.rbac.ClusterRole;

@Dependent
public class ClusterRoleWatcher extends ResourceWatcher<ClusterRole> {

    public ClusterRoleWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, ClusterRole resource) {
        super.eventReceived(action, resource);
    }

}