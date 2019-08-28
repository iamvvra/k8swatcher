package com.k8swatcher;

import javax.enterprise.context.Dependent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.fabric8.kubernetes.api.model.Event;
import io.fabric8.kubernetes.api.model.ObjectReference;

@Dependent
public class EventsWatcher extends ResourceWatcher<Event> {
    private static final Logger log = LoggerFactory.getLogger(EventsWatcher.class);

    public EventsWatcher(WatchConfig watchConfig, NotificationPublisher notificationPublisher) {
        super(watchConfig, notificationPublisher);
    }

    @Override
    public void eventReceived(Action action, Event resource) {
        if (action.equals(Action.MODIFIED)) {
            log.debug("skipping {} {} event", resource.getKind(), action);
            return;
        }
        String lastTimestamp = resource.getLastTimestamp();
        if (config.isOldEvent(lastTimestamp)) {
            log.debug("Skipping old events, {} ", lastTimestamp);
            return;
        }
        if (!config.isWatchedResource(resource.getInvolvedObject().getKind())) {
            log.debug("skipping the event of this {} kind", resource.getInvolvedObject().getKind());
            return;
        }
        if (!config.isWatchedEventLevel(resource.getType())) {
            log.debug("skipping event level, " + resource.getKind());
            return;
        }
        notify(action, resource);
    }

    @Override
    protected EventMessage newEventMessage(Action action, Event resource) {
        ObjectReference involvedObject = resource.getInvolvedObject();
        String kind = involvedObject.getKind();
        log.debug("New event received :: action: " + action.name() + ", res: " + resource);
        EventMessage event = EventMessage.builder().action(action.name())
                .namespace(resource.getMetadata().getNamespace()).kind(resource.getKind()).refferedObjectKind(kind)
                .reason(resource.getReason()).eventType(resource.getType()).lastTime(resource.getLastTimestamp())
                .firstTime(resource.getFirstTimestamp()).message(resource.getMessage()).cluster(config.clusterName())
                .resourceName(involvedObject.getName()).build();
        log.info("New {} event received :: {}", action, event.eventDetailShort());
        return event;
    }

    @Override
    boolean isEventOld(Event type) {
        return config.isOldEvent(type.getLastTimestamp());
    }
}