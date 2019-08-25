package com.k8swatcher;

import java.io.IOException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.k8swatcher.notifier.Level;
import com.k8swatcher.notifier.mattermost.MattermostNotfier;
import com.k8swatcher.notifier.slack.SlackNotifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.eventbus.EventBus;

@ApplicationScoped
public class NotificationPublisher {

    private static final Logger log = LoggerFactory.getLogger(NotificationPublisher.class);

    private WatchConfig config;
    private MattermostNotfier mattermostNotfier;
    private SlackNotifier slackNotifier;
    private EventBus eventBus;

    @Inject
    public NotificationPublisher(WatchConfig config, MattermostNotfier mattermostNotfier, SlackNotifier slackNotifier,
            EventBus eventBus) {
        this.config = config;
        this.mattermostNotfier = mattermostNotfier;
        this.slackNotifier = slackNotifier;
        this.eventBus = eventBus;
    }

    public void notifyEvent(EventMessage event) {
        notifyMattermost(event);
    }

    private void notifyMattermost(EventMessage event) {
        if (config.isMatterMostEnabled()) {
            log.debug("Notifying {} event to Mattermost", event.kind());
            eventBus.send("mattermost", JsonUtil.asJsonString(event));
        }
        if (config.isSlackEnabled()) {
            log.debug("Notifying {} event to slack", event.kind());
            eventBus.send("slack", JsonUtil.asJsonString(event));
        }
    }

    public void sendMessage(String message, Level level) throws IOException {
        if (config.isMatterMostEnabled()) {
            mattermostNotfier.sendNotification(message, level);
        }
        if (config.isSlackEnabled()) {
            slackNotifier.sendNotification(message, level);
        }
    }

}