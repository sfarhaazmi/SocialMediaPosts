package com.example.socialmediaposts.service;

import com.example.socialmediaposts.model.CommentDTO;
import com.example.socialmediaposts.model.PostDTO;
import com.example.socialmediaposts.repository.CommentEntity;
import com.example.socialmediaposts.repository.ISpringDataPostRepository;
import com.example.socialmediaposts.repository.PostEntity;
import com.example.socialmediaposts.rest.response.CustomExceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    @Autowired
    private ISpringDataPostRepository postRepository;


    public Page<PostEntity> getAllPosts(Pageable pageable) {
        try {
            return postRepository.findAllByOrderByCreatedAtDesc(pageable);
        } catch (Exception ex) {
            logger.error("Failed to retrieve posts. Please try again later.", ex);
            throw new RuntimeException("Failed to retrieve posts. Please try again later.");
        }
    }

    public PostEntity createPost(PostEntity post) {
        try {
            return postRepository.save(post);
        } catch (DataIntegrityViolationException ex) {
            logger.error("Failed to create post due to data integrity violation.", ex);
            throw ex;
        } catch (Exception ex) {
            logger.error("Failed to create post. Please try again later.", ex);
            throw new RuntimeException("Failed to create post. Please try again later.");
        }
    }

    public PostEntity updatePost(Long id, PostDTO postDTO) {
        Optional<PostEntity> optionalPost = postRepository.findById(id);
        if (optionalPost.isPresent()) {
            PostEntity existingPost = optionalPost.get();
            existingPost.setTitle(postDTO.getTitle());
            existingPost.setDescription(postDTO.getDescription());
            existingPost.setUpdatedAt(LocalDateTime.now());
            List<CommentEntity> commentsToUpdate = new ArrayList<>();
            if (postDTO.getComments() != null) { // Check if comments are not null
                for (CommentDTO commentDTO : postDTO.getComments()) {
                    CommentEntity commentEntity = new CommentEntity();
                    commentEntity.setContent(commentDTO.getContent());
                    commentsToUpdate.add(commentEntity);
                }
            }
            existingPost.getComments().addAll(commentsToUpdate);
            return postRepository.save(existingPost);
        } else {
            logger.error("Post not found with id: {}", id);
            throw new CustomExceptions.PostNotFoundException(id);
        }
    }

    public void deletePost(Long id) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
        } else {
            logger.error("Post not found with id: {}", id);
            throw new CustomExceptions.PostNotFoundException(id);
        }
    }

    public Optional<PostEntity> getPostById(Long id) {
        Optional<PostEntity> post = postRepository.findById(id);
        if (post.isPresent()) {
            return post;
        } else {
            logger.error("Post not found with id: {}", id);
            throw new CustomExceptions.PostNotFoundException(id);
        }
    }
}
