package com.k8swatcher.notifier.mattermost;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class Message {
    private Attachment attachment;
    private String channelId;
    private String userId;
}