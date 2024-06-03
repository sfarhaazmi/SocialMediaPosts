package com.example.socialmediaposts.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class CommentRepositoryImpl implements ICommentRepository{
    private final ISpringDataCommentRepository springDataCommentRepository;

    @Autowired
    public CommentRepositoryImpl(ISpringDataCommentRepository springDataCommentRepository) {
        this.springDataCommentRepository = springDataCommentRepository;
    }

    @Override
    public Page<CommentEntity> findByPostIdOrderByCreatedAtDesc(Long postId, Pageable pageable) {
        return springDataCommentRepository.findByPostIdOrderByCreatedAtDesc(postId, pageable);
    }
}
