package com.k8swatcher.notifier.mattermost;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Setter;
import lombok.Singular;

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
    @Singular
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}