package com.k8swatcher;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static final <T> String asJsonString(T object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("error converting the object to json", e);
            throw new RuntimeException(e);
        }
    }

    public static final <T> T toObject(String json, Class<T> claz) {
        try {
            return mapper.readValue(json, claz);
        } catch (IOException e) {
            log.error("error creating object from json", e);
            throw new RuntimeException(e);
        }
    }
}