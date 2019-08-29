package com.k8swatcher;

import javax.enterprise.context.Dependent;

import io.fabric8.openshift.api.model.OAuthClient;

@Dependent
public class OAuthClientWatcher extends ResourceWatcher<OAuthClient> {

    public OAuthClientWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, OAuthClient resource) {
        super.eventReceived(action, resource);
    }

}