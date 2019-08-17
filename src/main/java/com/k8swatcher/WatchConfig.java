package com.k8swatcher;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class WatchConfig {
    @ConfigProperty(name = "k8swatcher.resources", defaultValue = "ALL")
    private Set<Resource> resources;

    @ConfigProperty(name = "k8swatcher.namespaces", defaultValue = "ALL")
    private Set<String> namespaces;

    @ConfigProperty(name = "k8swatcher.event-levels", defaultValue = "ALL")
    private Set<EventLevel> eventLevels;

    @ConfigProperty(name = "k8swatcher.cluster-name", defaultValue = "unnamed-cluster")
    private String clusterName;

    @ConfigProperty(name = "k8swatcher.event-from-last-seconds", defaultValue = "15")
    private int lastEventTimeInSeconds;

    @ConfigProperty(name = "k8swatcher.mattermost-enabled", defaultValue = "false")
    private boolean matterMostEnabled;

    private ZonedDateTime startTime;

    private final String startupMessage = ":eyes: `k8swatcher` started - cluster `%s`";

    private final String shutdownMessage = ":ghost: `k8swatcher` shutdown down - no events are notified for this cluster `%s`";

    @PostConstruct
    private void setStartTime() {
        startTime = ZonedDateTime.now(ZoneId.of("Z").normalized());
    }

    public Set<String> getNamespaces() {
        return Collections.unmodifiableSet(namespaces);
    }

    public boolean watchAllNamespaces() {
        return namespaces == null || namespaces.isEmpty() || namespaces.contains("ALL");
    }

    public boolean isMatterMostEnabled() {
        return matterMostEnabled;
    }

    public boolean isWatchedResource(Resource resource) {
        return resources.isEmpty() || resources.contains(Resource.ALL) ? true : resources.contains(resource);
    }

    public Set<Resource> watchedResources() {
        return resources.isEmpty() || resources.contains(Resource.ALL)
                ? new HashSet<Resource>(Arrays.asList(Resource.values()))
                : resources;

    }

    public boolean isWatchedResource(String resource) {
        return Resource.valueof(resource.toUpperCase()).map(r -> isWatchedResource(r)).isPresent();
    }

    public boolean isValidReason(EventMessage message) {
        return Reason.valueof(message.getReason()).isPresent();
    }

    public boolean isWatchedEventLevel(String eventLevel) {
        return EventLevel.valueof(eventLevel.toUpperCase()).map(e -> eventLevels.contains(e)).get();
    }

    public String clusterName() {
        return clusterName;
    }

    public boolean isOldEvent(String eventTime) {
        ZonedDateTime evtTime = ZonedDateTime.parse(eventTime);

        long diffSec = startTime.until(evtTime, ChronoUnit.SECONDS);
        log.debug("last: " + evtTime + ", now: " + startTime + ", diffsec: " + diffSec);
        return diffSec < 0;
    }

    public String startupMessage() {
        return startupMessage;
    }

    public String shutdownMessage() {
        return shutdownMessage;
    }

}