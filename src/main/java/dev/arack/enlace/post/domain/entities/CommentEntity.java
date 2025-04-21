package dev.arack.enlace.post.domain.entities;

import dev.arack.enlace.shared.model.AuditableModel;

public class CommentEntity extends AuditableModel {
    private Long id;
    private String content;
    private Long userId;
    private Long postId;
}
