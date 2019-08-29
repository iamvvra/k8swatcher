package com.k8swatcher;

import javax.enterprise.context.Dependent;

import io.fabric8.openshift.api.model.Route;

@Dependent
public class RouteWatcher extends ResourceWatcher<Route> {

    public RouteWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, Route resource) {
        super.eventReceived(action, resource);
    }

}