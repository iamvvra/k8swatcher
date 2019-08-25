package com.k8swatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.Watcher;

public class ResourceWatcher<T extends HasMetadata> implements Watcher<T> {

    private static final Logger log = LoggerFactory.getLogger(ResourceWatcher.class);

    protected WatchConfig config;
    private NotificationPublisher notificationPublisher;

    public ResourceWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        this.config = config;
        this.notificationPublisher = notificationPublisher;
    }

    @Override
    public void eventReceived(Action action, T resource) {
        if (!isActionWatched(action)) {
            log.debug("skip resource action, " + action + ", res, " + resource);
            return;
        }
        if (isEventOld(resource)) {
            log.debug("skip old resource events, action: " + action + ", res:" + resource.getKind());
            return;
        }
        if (!isWatchedResource(resource)) {
            log.debug("skip unwatched resource, : action, " + action + ", res:" + resource.getKind());
            return;
        }
        notify(newEventMessage(action, resource));
    }

    protected void notify(EventMessage event) {
        notificationPublisher.notifyEvent(event);
    }

    protected EventMessage newEventMessage(Action action, T resource) {
        EventMessage newEventMessage = EventMessage.builder().action(action.name())
                .namespace(resource.getMetadata().getNamespace()).kind(resource.getKind())
                .creationTime(resource.getMetadata().getCreationTimestamp())
                .deletedTime(resource.getMetadata().getDeletionTimestamp()).cluster(config.clusterName())
                .resourceName(resource.getMetadata().getName()).build();
        log.debug("new object event received, " + action + ", " + resource);
        log.info("New {} event for {} received, {}", action, newEventMessage.kind(),
                newEventMessage.eventDetailShort());
        return newEventMessage;
    }

    public void onClose(KubernetesClientException cause) {
        log.error("closing kubernetes client", cause);
    }

    String getLastTimestamp(T type) {
        return type.getMetadata().getCreationTimestamp();
    }

    String creationTime(T type) {
        return type.getMetadata().getCreationTimestamp();
    }

    String deletionTime(T type) {
        return type.getMetadata().getDeletionTimestamp();
    }

    boolean isActionWatched(Action action) {
        return action.equals(Action.MODIFIED) ? false : true;
    }

    boolean isEventOld(T type) {
        String deletionTime = deletionTime(type);
        if (deletionTime != null) {
            return config.isOldEvent(deletionTime);
        }
        return config.isOldEvent(creationTime(type));
    }

    boolean isWatchedResource(T resource) {
        return config.isWatchedResource(resource.getKind());
    }
}