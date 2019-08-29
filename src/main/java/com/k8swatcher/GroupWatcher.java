package com.k8swatcher;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import io.fabric8.openshift.api.model.Group;

@Dependent
public class GroupWatcher extends ResourceWatcher<Group> {

    @Inject
    public GroupWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, Group resource) {
        super.eventReceived(action, resource);
    }

}