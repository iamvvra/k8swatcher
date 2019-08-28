package com.k8swatcher;

import javax.enterprise.context.Dependent;

import io.fabric8.kubernetes.api.model.HorizontalPodAutoscaler;

@Dependent
public class HorizontalPodAutoscalerWatcher extends ResourceWatcher<HorizontalPodAutoscaler> {

    public HorizontalPodAutoscalerWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, HorizontalPodAutoscaler resource) {
        super.eventReceived(action, resource);
    }

}