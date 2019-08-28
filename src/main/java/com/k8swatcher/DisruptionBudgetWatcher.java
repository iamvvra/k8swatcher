package com.k8swatcher;

import javax.enterprise.context.Dependent;

import io.fabric8.kubernetes.api.model.policy.PodDisruptionBudget;

@Dependent
public class DisruptionBudgetWatcher extends ResourceWatcher<PodDisruptionBudget> {

    public DisruptionBudgetWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, PodDisruptionBudget resource) {
        super.eventReceived(action, resource);
    }

}