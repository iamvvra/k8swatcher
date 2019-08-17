package com.k8swatcher.notifier.mattermost;

import java.util.HashMap;
import java.util.Map;

public class Field {

    public Boolean _short;
    public String title;
    public String value;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}