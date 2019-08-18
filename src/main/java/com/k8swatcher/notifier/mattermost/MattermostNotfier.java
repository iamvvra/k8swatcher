package com.k8swatcher.notifier.mattermost;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.k8swatcher.EventMessage;
import com.k8swatcher.notifier.Level;
import com.k8swatcher.notifier.Notifier;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class MattermostNotfier implements Notifier {

    private static final String NORMAL_COLOR = "#2683e1";
    private static final String WARN_COLOR = "#ff5733";
    private String host;
    private String token;
    private String channelId;
    private MattermostConnection mmConnection;
    private String userId;
    private String userDisplayName;

    @Inject
    public MattermostNotfier(@ConfigProperty(name = "k8swatcher.mattermost-host") String host,
            @ConfigProperty(name = "k8swatcher.mattermost-api-token") String token,
            @ConfigProperty(name = "k8swatcher.mattermost-channel-id") String channelId,
            @ConfigProperty(name = "k8swatcher.mattermost-user-id") String userId,
            @ConfigProperty(name = "k8swatcher.mattermost-user-display-name") String userDisplayName) {
        this.host = host;
        this.token = token;
        this.channelId = channelId;
        this.userId = userId;
        this.userDisplayName = userDisplayName;
    }

    @PostConstruct
    public void connect() {
        if (host == null && token == null)
            return;
        mmConnection = MattermostConnection.connect(host, token);
    }

    @Override
    public void sendNotification(EventMessage eventMessage) {
        Message message = formatedEventMessage(eventMessage);
        mmConnection.post(message);
    }

    @Override
    public void sendNotification(String message, Level level) {
        Message msg = createMessage();
        msg.setAttachment(createAttachment("", getColor(level), message));
        mmConnection.post(msg);
    }

    private Message formatedEventMessage(EventMessage eventMessage) {
        Message message = createMessage();

        String textMessage = eventMessage.message();
        Attachment attachment = createAttachment(eventMessage.title(), getColor(eventMessage), textMessage);

        message.setAttachment(attachment);
        return message;
    }

    private Attachment createAttachment(String title, String color, String message) {
        return Attachment.builder().title(title).authorName(userDisplayName).text(message).color(color).build();
    }

    private Message createMessage() {
        return Message.builder().channelId(channelId).userId(userId).build();
    }

    private String getColor(EventMessage eventMessage) {
        return eventMessage.isWarning() ? WARN_COLOR : NORMAL_COLOR;
    }

    private String getColor(Level level) {
        return level.equals(Level.WARNING) ? WARN_COLOR : NORMAL_COLOR;
    }

    @PreDestroy
    public void destroy() {
        mmConnection.close();
    }

}