package com.k8swatcher.notifier.mattermost;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Post {

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
    @JsonProperty("create_at")
    private long createAt;
    @JsonProperty("update_at")
    private long updateAt;
    @JsonProperty("edit_at")
    private long editAt;
    @JsonProperty("delete_at")
    private long deleteAt;
    @JsonProperty("is_pinned")
    private boolean isPinned;
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
    @JsonProperty("hashtags")
    private String hashtags;
    @JsonProperty("filenames")
    @Deprecated // do not use this field any more
    private List<String> filenames;
    @JsonProperty("file_ids")
    private List<String> fileIds;
    @JsonProperty("pending_post_id")
    private String pendingPostId;
    @JsonProperty("has_reactions")
    private boolean hasReactions;
    /* @since Mattermost Server 5.8 */
    // private PostMetadata metadata;

    public void attach(Attachment value) {
        if (props == null)
            props = new HashMap<>();
        props.put("attachments", Arrays.asList(value));
    }

}