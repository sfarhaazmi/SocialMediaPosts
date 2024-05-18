package com.example.socialmediaposts.service;

import com.example.socialmediaposts.model.PostDTO;
import com.example.socialmediaposts.repository.ISpringDataPostRepository;
import com.example.socialmediaposts.repository.PostEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    private ISpringDataPostRepository postRepository;


    public List<PostEntity> getAllPosts(int limit) {
        return postRepository.findAll(PageRequest.of(0, limit)).getContent();
    }

    public PostEntity createPost(PostEntity post) {
        return postRepository.save(post);
    }

    public PostEntity updatePost(Long id, PostDTO postDTO) {
        Optional<PostEntity> optionalPost = postRepository.findById(id);
        if (optionalPost.isPresent()) {
            PostEntity existingPost = optionalPost.get();
            existingPost.setTitle(postDTO.getTitle());
            existingPost.setDescription(postDTO.getDescription());
            existingPost.setUpdatedAt(LocalDateTime.now());
            return postRepository.save(existingPost);
        }
        return null;
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    public Optional<PostEntity> getPostById(Long id) {
        return postRepository.findById(id);
    }
}
