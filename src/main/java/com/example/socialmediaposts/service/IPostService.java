package com.example.socialmediaposts.service;

import com.example.socialmediaposts.model.PostDTO;
import com.example.socialmediaposts.repository.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IPostService {
    Page<PostEntity> getAllPosts(Pageable pageable);

    PostEntity createPost(PostDTO postDTO);

    PostEntity updatePost(Long id, PostDTO postDTO);

    void deletePost(Long id);

    Optional<PostEntity> getPostById(Long id);
}

