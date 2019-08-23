package com.k8swatcher.notifier.mattermost;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Builder;
import lombok.Setter;
import lombok.ToString;

@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
@RegisterForReflection
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Attachment implements Serializable {

    private static final long serialVersionUID = 1L;
    public String fallback;
    public String color;
    public String pretext;
    public String text;
    public String authorName;
    public String authorIcon;
    public String authorLink;
    public String title;
    public String titleLink;
    public List<Field> fields = null;
    public String imageUrl;

}