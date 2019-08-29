package com.k8swatcher;

import javax.enterprise.context.Dependent;

import io.fabric8.openshift.api.model.User;

@Dependent
public class UserWatcher extends ResourceWatcher<User> {

    public UserWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, User resource) {
        super.eventReceived(action, resource);
    }

}