package com.example.socialmediaposts.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICommentRepository {
    Page<CommentEntity> findByPostIdOrderByCreatedAtDesc(Long postId, Pageable pageable);
}
