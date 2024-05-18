package com.example.socialmediaposts.service;

import com.example.socialmediaposts.repository.CommentEntity;
import com.example.socialmediaposts.repository.ISpringDataCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    @Autowired
    private ISpringDataCommentRepository commentRepository;

    public Page<CommentEntity> getComments(Long postId, Pageable pageable) {
        return commentRepository.findByPostIdOrderByCreatedAtDesc(postId, pageable);
    }
}
