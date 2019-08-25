package com.k8swatcher.notifier;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.JsonProcessingException;
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
public class BasicHttpClient {
    private OkHttpClient httpClient;
    private final ObjectMapper mapper = new ObjectMapper();

    public BasicHttpClient() {
        httpClient = new OkHttpClient.Builder().readTimeout(0, TimeUnit.MILLISECONDS).build();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    public <T> Request createRequest(String url, HttpMethod method, Map<String, String> headers, T body)
            throws JsonProcessingException {
        log.debug("create post request\nresponse-code: {}\n{}\n{}", url, method, body(body));
        return new Request.Builder().url(url).method(method.name(), createBody(body)).headers(createHeader(headers))
                .build();
    }

    private Headers createHeader(Map<String, String> headers) {
        return Headers.of(headers);
    }

    protected <T> String body(T body) throws JsonProcessingException {
        return mapper.writeValueAsString(body);
    }

    public <T> RequestBody createBody(T body) throws JsonProcessingException {
        String postBody = mapper.writeValueAsString(body);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), postBody);
        return requestBody;
    }

    public Headers authHeader(String authToken) {
        return new Headers.Builder().add("Authorization", "Bearer " + authToken).build();
    }

    public Response execute(Request request) throws IOException {
        return httpClient.newCall(request).execute();
    }

    public <T> int execute(String url, HttpMethod method, Map<String, String> headers, T body) throws IOException {
        log.debug("create post request\nresponse-code: {}\n{}\n{}", url, method, body(body));
        Request request = new Request.Builder().url(url).method(method.name(), createBody(body))
                .headers(createHeader(headers)).build();
        Response response = httpClient.newCall(request).execute();
        log.debug("create post response\n{}\n{}\n{}", response.code(), response.headers(), response.body().string());
        int code = response.code();
        response.close();
        return code;
    }

    public Map<String, String> authHeaderMap(String authToken) {
        Map<String, String> authHeader = new HashMap<>();
        authHeader.put("Authorization", "Bearer " + authToken);
        return authHeader;
    }

    public enum HttpMethod {
        GET, POST, DELETE, PUT;
    }

}