package com.k8swatcher;

import java.io.Serializable;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@RegisterForReflection
@Getter
public class EventMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    private String action;
    private String namespace;
    private String message;
    private String reason;
    private String kind;
    private String eventType;
    private String resourceName;
    private String lastTime;
    private String firstTime;
    private String creationTime;
    private String deletedTime;
    private String cluster;
    private String refferedObjectKind;

    public EventMessage() {
    }

    public boolean isWarning() {
        if (isEvent() && isEventWarning())
            return true;
        if (action.equals("DELETED") || action.equals("ERROR"))
            return true;
        return false;
    }

    public boolean isDeleted() {
        return "DELETED".equals(action);
    }

    private boolean isEvent() {
        return "event".equalsIgnoreCase(kind);
    }

    private boolean isEventWarning() {
        return isEvent() ? ("warning".equalsIgnoreCase(eventType) ? true : false) : false;
    }

    public String title() {
        return resource() + " " + (isEventWarning() ? "error" : action.toLowerCase());
    }

    public String resource() {
        if (isEvent())
            return refferedObjectKind;
        return kind;
    }

    public String message() {
        String msg = "";
        if (isWarning() && isEvent()) {
            msg = String.format("%s `%s` has error, namespace `%s`, cluster `%s`", kind(), resourceName, namespace,
                    cluster);
        } else {
            msg = String.format("%s `%s` was %s in `%s` namespace of cluster `%s`", kind(), resourceName,
                    action.toLowerCase(), namespace, cluster);
        }
        if (message != null && !message.isEmpty()) {
            msg += String.format("\nCause, `%s`", message);
        }
        return msg;
    }

    public String time() {
        if (isEvent())
            return lastTime;
        if ("DELETED".equals(action))
            return deletedTime != null ? deletedTime : creationTime;
        if ("ADDED".equals(action))
            return creationTime;
        return creationTime;
    }

    public long timeInEpochSecond() {
        return ZonedDateTime.parse(time()).toEpochSecond();
    }

    public String kind() {
        return isEvent() ? refferedObjectKind : kind;
    }

    public String eventDetailShort() {
        return "kind:" + kind + ", name: " + resourceName + ", namespace: " + namespace + ", reason: " + reason;
    }
}