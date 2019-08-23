package com.k8swatcher.notifier.mattermost;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
@RegisterForReflection
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    public Post(String channelId, String message) {
        this.channelId = channelId;
        this.message = message;
    }

    public Post(String channelId, String userId, String message) {
        this.channelId = channelId;
        this.message = message;
        this.userId = userId;
    }

    @JsonProperty("id")
    private String id;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("channel_id")
    private String channelId;
    @JsonProperty("root_id")
    private String rootId;
    @JsonProperty("parent_id")
    private String parentId;
    @JsonProperty("original_id")
    private String originalId;
    @JsonProperty("message")
    private String message;
    @JsonProperty("props")
    private Map<String, Object> props;
    @JsonProperty("file_ids")
    private List<String> fileIds;
    @JsonProperty("pending_post_id")
    private String pendingPostId;
    @JsonProperty("has_reactions")
    private boolean hasReactions;

    public void attach(Attachment value) {
        if (props == null)
            props = new HashMap<>();
        props.put("attachments", Arrays.asList(value));
    }

}