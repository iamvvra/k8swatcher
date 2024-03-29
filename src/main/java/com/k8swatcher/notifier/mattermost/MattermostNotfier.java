package com.k8swatcher.notifier.mattermost;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.k8swatcher.EventMessage;
import com.k8swatcher.JsonUtil;
import com.k8swatcher.notifier.Level;
import com.k8swatcher.notifier.Notifier;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.vertx.ConsumeEvent;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class MattermostNotfier implements Notifier {

    private static final String NORMAL_COLOR = "#2683e1";
    private static final String WARN_COLOR = "#ff5733";
    private String host;
    private String token;
    private String channelId;
    private MattermostClient mattermostClient;
    private String userId;
    private String userDisplayName;
    private Boolean enabled;

    @Inject
    public MattermostNotfier(@ConfigProperty(name = "k8swatcher.mattermost-enabled") Boolean enabled,
            @ConfigProperty(name = "k8swatcher.mattermost-host") String host,
            @ConfigProperty(name = "k8swatcher.mattermost-api-token") String token,
            @ConfigProperty(name = "k8swatcher.mattermost-channel-id") String channelId,
            @ConfigProperty(name = "k8swatcher.mattermost-user-id") String userId,
            @ConfigProperty(name = "k8swatcher.mattermost-user-display-name") String userDisplayName) {
        this.enabled = enabled;
        this.host = host;
        this.token = token;
        this.channelId = channelId;
        this.userId = userId;
        this.userDisplayName = userDisplayName;
    }

    @PostConstruct
    public void connect() {
        if (!enabled)
            return;
        if (host == null && token == null)
            return;
        try {
            mattermostClient = MattermostClient.connect(host, token);
            log.info("Mattermost connected, " + host);
        } catch (IOException e) {
            log.error("Error connecting Mattermost " + host, e);
        }
    }

    @ConsumeEvent(value = "mattermost")
    public void notify(String message) {
        if (!enabled)
            return;
        EventMessage eventMessage = JsonUtil.toObject(message, EventMessage.class);
        sendNotification(eventMessage);
    }

    @Override
    public void sendNotification(EventMessage eventMessage) {
        try {
            mattermostClient.post(postWithAttachment(eventMessage));
        } catch (IOException e) {
            log.error("Error sending message", e);
        }
    }

    @Override
    public void sendNotification(String message, Level level) throws IOException {
        Post post = newPost("");
        post.attach(createAttachment("", getColor(level), message));
        mattermostClient.post(post);
    }

    private Post postWithAttachment(EventMessage eventMessage) {
        Post post = newPost(null);
        post.attach(createAttachment(eventMessage.title(), getColor(eventMessage),
                (eventMessage.message() + "\nTime `" + eventMessage.time() + "`")));
        return post;
    }

    private Post newPost(String message) {
        return new Post(channelId, userId, message);
    }

    private Attachment createAttachment(String title, String color, String message) {
        return Attachment.builder().title(title).authorName(userDisplayName).text(message).color(color).build();
    }

    private String getColor(EventMessage eventMessage) {
        return eventMessage.isWarning() ? WARN_COLOR : NORMAL_COLOR;
    }

    private String getColor(Level level) {
        return level.equals(Level.WARNING) ? WARN_COLOR : NORMAL_COLOR;
    }

    @PreDestroy
    public void destroy() {
        mattermostClient.close();
    }

}