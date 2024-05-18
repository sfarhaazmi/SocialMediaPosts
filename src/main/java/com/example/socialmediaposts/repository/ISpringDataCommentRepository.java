package com.example.socialmediaposts.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISpringDataCommentRepository extends JpaRepository<CommentEntity, Long> {
    Page<CommentEntity> findByPostIdOrderByCreatedAtDesc(Long postId, Pageable pageable);
}
