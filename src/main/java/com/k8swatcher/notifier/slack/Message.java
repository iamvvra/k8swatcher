package com.k8swatcher.notifier.slack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@RegisterForReflection
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private String channel;
    private String text;
    @Setter(value = AccessLevel.NONE)
    private List<MessageAttachment> attachments;

    public Message attach(MessageAttachment attachment) {
        if (attachments == null) {
            attachments = new ArrayList<>();
        }
        this.attachments.add(attachment);
        return this;
    }

}