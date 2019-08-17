package com.k8swatcher;

import java.util.Optional;
import java.util.stream.Stream;

public enum Reason {
    BACKOFF, FAILED, FAILEDSYNC, FAILEDGETSCALE, FAILEDCREATE, FAILEDMOUNT, KILLING, CREATED, SUCCESSFULCREATE;

    public static Optional<Reason> valueof(String name) {
        return Stream.of(Reason.values()).filter(r -> r.name().equals(name)).findFirst();
    }
}