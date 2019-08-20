package com.k8swatcher;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.k8swatcher.notifier.Level;
import com.k8swatcher.notifier.mattermost.MattermostNotfier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class NotificationPublisher {

    private static final Logger log = LoggerFactory.getLogger(NotificationPublisher.class);

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