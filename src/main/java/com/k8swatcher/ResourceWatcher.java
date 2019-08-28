package com.k8swatcher;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.Watcher;
import io.vertx.reactivex.core.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;

@Dependent
@Slf4j
public class ResourceWatcher<T extends HasMetadata> extends WatchVerticle implements Watcher<T> {

    protected WatchConfig config;

    @Inject
    private EventBus eventBus;

    @Inject
    public ResourceWatcher(WatchConfig config, NotificationPublisher notificationPublisher) {
        super(notificationPublisher);
        this.config = config;
    }

    @Override
    public void eventReceived(Action action, T resource) {
        if (!isActionWatched(action)) {
            log.debug("skip {} {} action, ", resource.getKind(), action);
            return;
        }
        if (isEventOld(resource)) {
            log.debug("skip old {} {} event", resource.getKind(), action);
            return;
        }
        if (!isWatchedResource(resource)) {
            log.debug("skip unwatched object {}, {}", resource.getKind(), action);
            return;
        }
        notify(action, resource);
    }

    protected EventBus notify(Action action, T resource) {
        log.debug("sending {} {} to address {}, in eventbus ", resource.getKind(), action, getAddress());
        return eventBus.send(getAddress(), JsonUtil.asJsonString(newEventMessage(action, resource)));
    }

    EventMessage newEventMessage(Action action, T resource) {
        EventMessage newEventMessage = EventMessage.builder().action(action.name())
                .namespace(resource.getMetadata().getNamespace()).kind(resource.getKind())
                .creationTime(resource.getMetadata().getCreationTimestamp())
                .deletedTime(resource.getMetadata().getDeletionTimestamp()).cluster(config.clusterName())
                .resourceName(resource.getMetadata().getName()).build();
        log.info("new {} {} event received", resource.getKind(), action);
        log.debug("new {} {} event received, {}", newEventMessage.kind(), action, newEventMessage.eventDetailShort());
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