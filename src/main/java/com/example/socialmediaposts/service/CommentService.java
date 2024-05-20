package com.example.socialmediaposts.service;

import com.example.socialmediaposts.repository.CommentEntity;
import com.example.socialmediaposts.repository.ISpringDataCommentRepository;
import com.example.socialmediaposts.rest.response.CustomExceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    @Autowired
    private ISpringDataCommentRepository commentRepository;

    public Page<CommentEntity> getComments(Long postId, Pageable pageable) {
        try {
            Page<CommentEntity> comments = commentRepository.findByPostIdOrderByCreatedAtDesc(postId, pageable);
            logger.info("Fetched {} comments for post with ID: {}", comments.getTotalElements(), postId);
            return comments;
        } catch (Exception ex) {
            logger.error("Failed to retrieve comments for post with ID: {}", postId, ex);
            throw new CustomExceptions.CommentNotFoundException(postId);
        }
    }
}
