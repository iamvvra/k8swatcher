package com.k8swatcher;

import java.io.IOException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.k8swatcher.notifier.Level;
import com.k8swatcher.notifier.mattermost.MattermostNotfier;
import com.k8swatcher.notifier.slack.SlackNotifier;

import io.vertx.core.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class NotificationPublisher {

    private WatchConfig config;
    private EventBus eventBus;

    private MattermostNotfier mattermostNotfier;
    private SlackNotifier slackNotifier;

    @Inject
    public NotificationPublisher(WatchConfig config, MattermostNotfier mattermostNotfier, SlackNotifier slackNotifier,
            EventBus eventBus) {
        this.config = config;
        this.mattermostNotfier = mattermostNotfier;
        this.slackNotifier = slackNotifier;
        this.eventBus = eventBus;
    }

    public void notifyEvent(EventMessage event) {
        notifyEnabledChannels(event);
    }

    private void notifyEnabledChannels(EventMessage event) {
        if (config.isMatterMostEnabled()) {
            log.debug("Notifying {}-{} event to {} channel", event.getAction(), event.kind(), "mattermost");
            eventBus.send("mattermost", JsonUtil.asJsonString(event));
        }
        if (config.isSlackEnabled()) {
            log.debug("Notifying {}-{} event to {} channel", event.getAction(), event.kind(), "slack");
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