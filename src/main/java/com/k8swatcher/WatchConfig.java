package com.k8swatcher;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class WatchConfig {
    private static final Logger log = LoggerFactory.getLogger(WatchConfig.class);

    private final String startupMessage = ":eyes: `k8swatcher` started - cluster `%s`";

    private final String shutdownMessage = ":ghost: `k8swatcher` shutdown down - no events of `%s` cluster are notified";

    @Inject
    @ConfigProperty(name = "k8swatcher.resources", defaultValue = "ALL")
    private Set<String> resources;

    @Inject
    @ConfigProperty(name = "k8swatcher.namespaces", defaultValue = "ALL")
    private Set<String> namespaces;

    @Inject
    @ConfigProperty(name = "k8swatcher.event-levels", defaultValue = "ALL")
    private Set<String> eventLevels;

    @Inject
    @ConfigProperty(name = "k8swatcher.cluster-name", defaultValue = "unnamed-cluster")
    private String clusterName;

    @Inject
    @ConfigProperty(name = "k8swatcher.event-from-last-seconds", defaultValue = "15")
    private int lastEventTimeInSeconds;

    @Inject
    @ConfigProperty(name = "k8swatcher.mattermost-enabled", defaultValue = "false")
    private boolean matterMostEnabled;

    private ZonedDateTime startTime = ZonedDateTime.now(ZoneId.of("Z").normalized());

    @ConfigProperty(name = "k8swatcher.slack-enabled", defaultValue = "false")
    private boolean slackEnabled;

    public ZonedDateTime startTime() {
        return startTime;
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

    public boolean isWatchedResource(String resource) {
        return resources.isEmpty() || resources.contains("ALL") ? true : resources.contains(resource.toUpperCase());
    }

    public Set<String> watchedResources() {
        return resources.isEmpty() || resources.contains("ALL")
                ? Stream.of(Resource.values()).map(r -> r.name()).collect(Collectors.toSet())
                : resources;

    }

    public boolean isValidReason(EventMessage message) {
        return Reason.valueof(message.getReason()).isPresent();
    }

    public boolean isWatchedEventLevel(String eventLevel) {
        return eventLevels.contains(eventLevel.toUpperCase());
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

    public boolean isSlackEnabled() {
        return slackEnabled;
    }

}