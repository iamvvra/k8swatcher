package com.k8swatcher;

import java.util.Optional;
import java.util.stream.Stream;

public enum EventLevel {
    WARNING, ERROR, NORMAL;

    public static Optional<EventLevel> valueof(String event) {
        return Stream.of(EventLevel.values()).filter(e -> e.name().equalsIgnoreCase(event)).findFirst();
    }
}