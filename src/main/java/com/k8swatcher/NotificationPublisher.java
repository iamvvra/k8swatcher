package com.k8swatcher;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.k8swatcher.notifier.Level;
import com.k8swatcher.notifier.mattermost.MattermostNotfier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.eventbus.EventBus;

@ApplicationScoped
public class NotificationPublisher {

    private static final Logger log = LoggerFactory.getLogger(NotificationPublisher.class);

    private WatchConfig config;
    private MattermostNotfier mattermostNotfier;
    private EventBus eventBus;

    @Inject
    public NotificationPublisher(WatchConfig config, MattermostNotfier mattermostNotfier, EventBus eventBus) {
        this.config = config;
        this.mattermostNotfier = mattermostNotfier;
        this.eventBus = eventBus;
    }

    public void notifyEvent(EventMessage event) {
        notifyMattermost(event);
    }

    private void notifyMattermost(EventMessage event) {
        if (config.isMatterMostEnabled()) {
            log.debug("Notifying {} event to Mattermost", event.getKind());
            eventBus.send("mattermost", JsonUtil.asJsonString(event));
        }
    }

    public void sendMessage(String message, Level level) {
        if (config.isMatterMostEnabled()) {
            mattermostNotfier.sendNotification(message, level);
        }
    }

}