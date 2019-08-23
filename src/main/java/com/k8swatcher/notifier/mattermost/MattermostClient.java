package com.k8swatcher.notifier.mattermost;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Slf4j
public class MattermostClient {
    private OkHttpClient httpClient;
    private String mmHostUrl;
    private String apiToken;
    private static final String apiVersion = "/api/v4";
    private final ObjectMapper mapper = new ObjectMapper();

    public static MattermostClient connect(String host, String token) throws IOException {
        log.debug("establishing the connection to mattermost " + host);
        MattermostClient mmClient = new MattermostClient(host, token);
        if (!mmClient.connect()) {
            throw new RuntimeException("Error connecting to the mattermost");
        }
        log.debug("connected to mattermost " + host);
        return mmClient;
    }

    public MattermostClient(String mmHostUrl, String apiToken) {
        this.mmHostUrl = mmHostUrl;
        this.apiToken = apiToken;
        httpClient = new OkHttpClient.Builder().readTimeout(0, TimeUnit.MILLISECONDS).build();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    public boolean connect() throws IOException {
        Request request = createRequest("/users/me/status", "GET", null);
        Response response = httpClient.newCall(request).execute();
        boolean status = response.isSuccessful();
        log.debug("check status: " + (status ? "success" : "failed"));
        response.close();
        return status;
    }

    private Request createRequest(String mmResource, String method, RequestBody body) {
        return new Request.Builder().url(mmHostUrl + apiVersion + mmResource).headers(authHeader()).method(method, body)
                .build();
    }

    private Headers authHeader() {
        return new Headers.Builder().add("Authorization", "Bearer " + apiToken).build();
    }

    public void close() {
        httpClient.dispatcher().executorService().shutdown();
    }

    public void post(Post post) throws IOException {
        log.debug("create the post, " + post.toString());
        String postBody = new ObjectMapper().writeValueAsString(post);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), postBody);
        Request request = createRequest("/posts", "POST", body);
        log.debug("create post request\nresponse-code: {}\n{}\n{}", request.url(), request.headers(), postBody);
        Response resp = httpClient.newCall(request).execute();
        log.debug("create post response\n{}\n{}\n{}", resp.code(), resp.headers(), resp.body().string());
        resp.close();
    }
}