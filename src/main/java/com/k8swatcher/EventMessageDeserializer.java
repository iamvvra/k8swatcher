package com.k8swatcher;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class EventMessageDeserializer extends JsonDeserializer<EventMessage> {

    @Override
    public EventMessage deserialize(JsonParser parser, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode tree = parser.getCodec().readTree(parser);
        return EventMessage.builder().action(tree.get("action").textValue()).cluster(tree.get("cluster").textValue())
                .creationTime(tree.get("creationTime").textValue()).deletedTime(tree.get("deletedTime").textValue())
                .eventType(tree.get("eventType").textValue()).firstTime(tree.get("firstTime").textValue())
                .kind(tree.get("kind").textValue()).lastTime(tree.get("lastTime").textValue())
                .message(tree.get("message").textValue()).namespace(tree.get("namespace").textValue())
                .reason(tree.get("reason").textValue()).refferedObjectKind(tree.get("refferedObjectKind").textValue())
                .resourceName(tree.get("resourceName").textValue()).build();
    }

}