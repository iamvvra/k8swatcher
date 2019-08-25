package com.k8swatcher;

import io.fabric8.openshift.api.model.ImageStream;

public class ImageStreamWatcher extends ResourceWatcher<ImageStream> {

    public ImageStreamWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, ImageStream resource) {
        super.eventReceived(action, resource);
    }

}