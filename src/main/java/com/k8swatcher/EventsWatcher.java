package com.k8swatcher;

import com.k8swatcher.notifier.Notifier.Level;

import io.fabric8.kubernetes.api.model.Event;
import io.fabric8.kubernetes.api.model.ObjectReference;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.Watcher;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EventsWatcher implements Watcher<Event> {
    private WatchConfig watchConfig;
    private NotificationPublisher notificationPublisher;

    public EventsWatcher(WatchConfig watchConfig, NotificationPublisher notificationPublisher) {
        this.watchConfig = watchConfig;
        this.notificationPublisher = notificationPublisher;
    }

    @Override
    public void eventReceived(Action action, Event resource) {
        if (action.equals(Action.MODIFIED)) {
            log.debug("skipping event, " + action + ", resource: " + resource);
            return;
        }
        String lastTimestamp = resource.getLastTimestamp();
        if (!watchConfig.isOldEvent(lastTimestamp)) {
            log.debug("Skipping old events, " + lastTimestamp);
            return;
        }
        if (!watchConfig.isWatchedResource(resource.getInvolvedObject().getKind())) {
            log.debug("skipping the event of this kind, " + resource.getInvolvedObject().getKind());
            return;
        }
        if (!watchConfig.isWatchedEventLevel(resource.getType())) {
            log.debug("skipping event level, " + resource);
            return;
        }
        log.debug("New event received :: action: " + action.name() + ", res: " + resource);

        ObjectReference involvedObject = resource.getInvolvedObject();
        String kind = involvedObject.getKind();
        EventMessage event = EventMessage.builder().action(action.name())
                .namespace(resource.getMetadata().getNamespace()).kind(resource.getKind()).refferedObjectKind(kind)
                .reason(resource.getReason()).eventType(resource.getType()).lastTime(lastTimestamp)
                .firstTime(resource.getFirstTimestamp()).message(resource.getMessage())
                .cluster(watchConfig.clusterName()).resourceName(involvedObject.getName()).build();
        log.info("New event received :: " + event.eventDetailShort());
        sendNotification(event);
    }

    @Override
    public void onClose(KubernetesClientException cause) {
        notificationPublisher.sendMessage("error - " + cause.getMessage(), Level.WARNING);
    }

    private void sendNotification(EventMessage message) {
        notificationPublisher.notifyEvent(message);
    }
}