package com.k8swatcher.notifier.mattermost;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.bis5.mattermost.client4.MattermostClient;
import net.bis5.mattermost.model.Post;

public class MattermostConnection {
    private MattermostClient client;

    private MattermostConnection(MattermostClient client) {
        this.client = client;

    }

    public void post(String message, String channel, String user) {
        send(createPost(message, user, channel));
    }

    public void post(Message message) {
        Post post = new Post();
        post.setChannelId(message.getChannelId());
        post.setUserId(message.getUserId());
        Map<String, Object> attachment = new HashMap<>();
        attachment.put("attachments", Arrays.asList(getProps(message)));
        post.setProps(attachment);
        send(post);
    }

    private void send(Post post) {
        client.createPost(post);
    }

    private Map<String, Object> getProps(Message message) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(message.getAttachment(), Map.class);
    }

    private Post createPost(String message, String user, String channel) {
        Post post = new Post(channel, message);
        post.setUserId(user);
        Map<String, Object> props = new LinkedHashMap<>();
        post.setProps(props);
        return post;
    }

    public static MattermostConnection connect(String host, String token) {
        MattermostClient client = MattermostClient.builder().url(host).ignoreUnknownProperties().logLevel(Level.SEVERE)
                .build();
        client.setAccessToken(token);
        return new MattermostConnection(client);
    }

    public void close() {
        client.clearOAuthToken();
        client.close();
    }

}