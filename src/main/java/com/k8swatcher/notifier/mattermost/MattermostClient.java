package com.k8swatcher.notifier.mattermost;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;

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

    public static MattermostClient connect(String host, String token) throws IOException {
        MattermostClient mmClient = new MattermostClient(host, token);
        mmClient.connect();
        return mmClient;
    }

    public MattermostClient(String mmHostUrl, String apiToken) {
        this.mmHostUrl = mmHostUrl;
        this.apiToken = apiToken;
        httpClient = new OkHttpClient.Builder().readTimeout(0, TimeUnit.MILLISECONDS).build();
    }

    public boolean connect() throws IOException {
        Request request = createRequest("/users/me/status", "GET", null);
        Response response = httpClient.newCall(request).execute();
        boolean status = response.isSuccessful();
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
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                new ObjectMapper().writeValueAsString(post));
        Request request = new Request.Builder().header("Authorization", "Bearer " + apiToken)
                .url(mmHostUrl + apiVersion + "/posts").post(body).build();
        log.info(request.headers().toString());
        log.info(request.body().toString());
        Response resp = httpClient.newCall(request).execute();
        log.info("create post response", resp.body().string());
        resp.close();
    }
}