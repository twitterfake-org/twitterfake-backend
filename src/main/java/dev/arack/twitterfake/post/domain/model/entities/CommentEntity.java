package dev.arack.twitterfake.post.domain.model.entities;

import dev.arack.twitterfake.shared.model.AuditableModel;

public class CommentEntity extends AuditableModel {
    private Long id;
    private String content;
    private Long userId;
    private Long postId;
}
