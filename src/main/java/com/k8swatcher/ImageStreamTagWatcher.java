package com.k8swatcher;

import io.fabric8.openshift.api.model.ImageStreamTag;

public class ImageStreamTagWatcher extends ResourceWatcher<ImageStreamTag> {

    public ImageStreamTagWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(config, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, ImageStreamTag resource) {
        super.eventReceived(action, resource);
    }

}