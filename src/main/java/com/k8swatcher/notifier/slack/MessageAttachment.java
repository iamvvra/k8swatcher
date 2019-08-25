package com.k8swatcher.notifier.slack;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MessageAttachment implements Serializable {

    private static final long serialVersionUID = 1L;
    private String fallback;
    private String color;
    private String pretext;
    private String text;
    private String authorName;
    private String authorIcon;
    private String authorLink;
    private String title;
    private String titleLink;
    private List<Field> fields = null;
    private String imageUrl;
    private long ts;
}