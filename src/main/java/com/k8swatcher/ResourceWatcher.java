package com.k8swatcher;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.Watcher;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResourceWatcher<T extends HasMetadata> implements Watcher<T> {
    private WatchConfig config;
    private NotificationPublisher notificationPublisher;

    public ResourceWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        this.config = config;
        this.notificationPublisher = notificationPublisher;
    }

    boolean isActionWatched(Action action) {
        return action.equals(Action.MODIFIED) ? false : true;
    }

    boolean isHistoricEvent(T type) {
        String deletionTime = deletionTime(type);
        if (deletionTime != null) {
            return config.isOldEvent(deletionTime);
        }
        return config.isOldEvent(creationTime(type));
    }

    boolean isWatchedResource(T resource) {
        return config.isWatchedResource(resource.getKind());
    }

    @Override
    public void eventReceived(Action action, T resource) {
        if (!isActionWatched(action)) {
            log.debug("skip action, " + action + ", res, " + resource);
            return;
        }
        if (isHistoricEvent(resource)) {
            log.debug("skip old events, action: " + action);
            return;
        }
        if (!isWatchedResource(resource)) {
            log.debug("skip unwatched resource, " + resource.getKind());
            return;
        }
        EventMessage newEventMessage = newEventMessage(action, resource);
        log.info("new event received, " + action + ", " + resource);
        processEvent(newEventMessage);
    }

    private EventMessage newEventMessage(Action action, T resource) {
        return EventMessage.builder().action(action.name()).namespace(resource.getMetadata().getNamespace())
                .kind(resource.getKind()).lastTime(resource.getMetadata().getCreationTimestamp())
                .firstTime(resource.getMetadata().getCreationTimestamp()).cluster(config.clusterName())
                .resourceName(resource.getMetadata().getName()).build();
    }

    public void onClose(KubernetesClientException cause) {
        cause.printStackTrace();
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

    public void processEvent(EventMessage eventMessage) {
        log.info("processing event, " + eventMessage);
        notificationPublisher.notifyEvent(eventMessage);
    }
}