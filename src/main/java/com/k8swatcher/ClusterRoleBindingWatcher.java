package com.k8swatcher;

import javax.enterprise.context.Dependent;

import io.fabric8.kubernetes.api.model.rbac.ClusterRoleBinding;

@Dependent
public class ClusterRoleBindingWatcher extends ResourceWatcher<ClusterRoleBinding> {

    public ClusterRoleBindingWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, ClusterRoleBinding resource) {
        super.eventReceived(action, resource);
    }

}