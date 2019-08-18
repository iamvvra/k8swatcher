package com.k8swatcher.notifier.mattermost;

import java.util.List;

import lombok.Builder;
import lombok.Setter;

@Setter
@Builder
public class Attachment {

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