package com.k8swatcher;

import javax.enterprise.context.Dependent;

import io.fabric8.openshift.api.model.Project;

@Dependent
public class ProjectWatcher extends ResourceWatcher<Project> {

    public ProjectWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, Project resource) {
        super.eventReceived(action, resource);
    }

}