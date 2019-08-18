package com.k8swatcher;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.k8swatcher.notifier.Level;
import com.k8swatcher.notifier.mattermost.MattermostNotfier;

import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class NotificationPublisher {

    private WatchConfig config;
    private MattermostNotfier mattermostNotfier;

    @Inject
    public NotificationPublisher(WatchConfig config, MattermostNotfier mattermostNotfier) {
        this.config = config;
        this.mattermostNotfier = mattermostNotfier;
    }

    public void notifyEvent(EventMessage event) {
        if (config.isMatterMostEnabled()) {
            log.debug("Notifying {} event Mattermost", event.getKind());
            mattermostNotfier.sendNotification(event);
        }
    }

    public void sendMessage(String message, Level level) {
        if (config.isMatterMostEnabled()) {
            mattermostNotfier.sendNotification(message, level);
        }
    }

}