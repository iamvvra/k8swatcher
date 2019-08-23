package com.k8swatcher;

import io.fabric8.kubernetes.api.model.policy.PodDisruptionBudget;

public class DisruptionBudgetWatcher extends ResourceWatcher<PodDisruptionBudget> {

    public DisruptionBudgetWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, PodDisruptionBudget resource) {
        super.eventReceived(action, resource);
    }

}