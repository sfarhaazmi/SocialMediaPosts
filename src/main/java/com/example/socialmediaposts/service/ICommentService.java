package com.example.socialmediaposts.service;

import com.example.socialmediaposts.repository.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICommentService {
    Page<CommentEntity> getComments(Long postId, Pageable pageable);
}
