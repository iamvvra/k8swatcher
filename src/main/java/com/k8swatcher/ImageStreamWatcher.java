package com.k8swatcher;

import javax.enterprise.context.Dependent;

import io.fabric8.openshift.api.model.ImageStream;

@Dependent
public class ImageStreamWatcher extends ResourceWatcher<ImageStream> {

    public ImageStreamWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, ImageStream resource) {
        super.eventReceived(action, resource);
    }

}