package com.k8swatcher.notifier.mattermost;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Setter;

@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
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