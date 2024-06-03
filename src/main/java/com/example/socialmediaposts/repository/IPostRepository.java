package com.example.socialmediaposts.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IPostRepository {
    Page<PostEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

    PostEntity save(PostEntity postEntity);

    Optional<PostEntity> findById(Long id);

    boolean existsById(Long id);

    void deleteById(Long id);
}

