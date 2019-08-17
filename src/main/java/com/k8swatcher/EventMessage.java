package com.k8swatcher;

import java.io.Serializable;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
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

    public boolean isWarning() {
        if (isEvent() && isEventWarning())
            return true;
        if (action.equals("DELETED") || action.equals("ERROR"))
            return true;
        return false;
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
        if (message != null) {
            return String.format("%s `%s` was %s in `%s` namespace of cluster `%s`\nTime `%s`\n`%s`", kind,
                    resourceName, action.toLowerCase(), namespace, cluster, lastTime, message);
        }
        return String.format("%s `%s` was %s in `%s` namespace of cluster `%s`\nTime `%s`", kind, resourceName,
                action.toLowerCase(), namespace, cluster, lastTime);
    }

    public String eventDetailShort() {
        return "kind:" + kind + ", name: " + resourceName + ", namespace: " + namespace + ", reason: " + reason;
    }
}