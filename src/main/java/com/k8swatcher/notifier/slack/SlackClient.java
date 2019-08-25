package com.k8swatcher.notifier.slack;

import java.io.IOException;

import com.k8swatcher.notifier.BasicHttpClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SlackClient extends BasicHttpClient {

    private static final String SLACK_API_URL = "https://slack.com/api";
    private static final String CREATE_POST_URI = "/chat.postMessage";
    private String token;

    private SlackClient(String token) {
        this.token = token;
    }

    public static SlackClient getClient(String apiToken) {
        return new SlackClient(apiToken);
    }

    public void createPost(Message post) throws IOException {
        log.debug("create slack post, " + post.toString());
        int code = execute(SLACK_API_URL + CREATE_POST_URI, HttpMethod.POST, authHeaderMap(token), post);
        log.debug("create slack post response: {}" + code);
    }

}