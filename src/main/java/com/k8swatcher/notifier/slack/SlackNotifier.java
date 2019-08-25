package com.k8swatcher.notifier.slack;

import java.io.IOException;
import java.time.Instant;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.k8swatcher.EventMessage;
import com.k8swatcher.JsonUtil;
import com.k8swatcher.notifier.Level;
import com.k8swatcher.notifier.Notifier;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.vertx.ConsumeEvent;

@ApplicationScoped
public class SlackNotifier implements Notifier {

    private static final String NORMAL_COLOR = "#36a64f";
    private static final String WARNING_COLOR = "#ff5733";
    private SlackClient slackClient;
    private String channelId;
    @ConfigProperty(name = "k8swatcher.slack-enabled")
    boolean enabled;

    @Inject
    public SlackNotifier(@ConfigProperty(name = "k8swatcher.slack-api-token") String apiToken,
            @ConfigProperty(name = "k8swatcher.slack-channel-id") String channelId) {
        this.channelId = channelId;
        this.slackClient = SlackClient.getClient(apiToken);
    }

    @ConsumeEvent(value = "slack")
    public void notify(String eventMessage) throws IOException {
        if (!enabled)
            return;
        sendNotification(JsonUtil.toObject(eventMessage, EventMessage.class));
    }

    @Override
    public void sendNotification(EventMessage eventMessage) throws IOException {
        slackClient.createPost(messageWithAttachment(eventMessage));
    }

    private Message messageWithAttachment(EventMessage eventMessage) {
        Message message = newMessage();
        message.attach(anAttachment(eventMessage.title(), eventMessage.message(), color(eventMessage.isWarning()),
                eventMessage.timeInEpochSecond()));
        return message;
    }

    private Message newMessage() {
        return Message.builder().channel(channelId).build();
    }

    private MessageAttachment anAttachment(String title, String message, String color, long ts) {
        return MessageAttachment.builder().title(title).text(message).color(color).ts(ts).build();
    }

    private String color(boolean warning) {
        return warning ? WARNING_COLOR : NORMAL_COLOR;
    }

    @Override
    public void sendNotification(String message, Level level) throws IOException {
        slackClient.createPost(newMessage()
                .attach(anAttachment("", message, color(level.equals(Level.WARNING)), Instant.now().getEpochSecond())));
    }

}