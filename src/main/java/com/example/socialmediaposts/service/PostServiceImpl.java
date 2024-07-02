package com.example.socialmediaposts.service;

import com.example.socialmediaposts.model.CommentDTO;
import com.example.socialmediaposts.model.PostDTO;
import com.example.socialmediaposts.repository.CommentEntity;
import com.example.socialmediaposts.repository.IPostRepository;
import com.example.socialmediaposts.repository.PostEntity;
import com.example.socialmediaposts.rest.response.CustomExceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements IPostService {
    private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    private final IPostRepository postRepository;

    @Autowired
    public PostServiceImpl(IPostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Page<PostDTO> getAllPosts(Pageable pageable) {
        try {
            // Fetch the Page<PostEntity> from the repository
            Page<PostEntity> postEntities = postRepository.findAllByOrderByCreatedAtDesc(pageable);

            // Convert each PostEntity to PostDTO using a mapper
            List<PostDTO> postDTOs = postEntities.getContent().stream()
                    .map(this::convertPostEntityToDTO)
                    .collect(Collectors.toList());

            // Create a new Page<PostDTO> with the converted data
            return new PageImpl<>(postDTOs, postEntities.getPageable(), postEntities.getTotalElements());
        } catch (Exception ex) {
            logger.error("Failed to retrieve posts. Please try again later.", ex);
            throw new RuntimeException("Failed to retrieve posts. Please try again later.");
        }
    }


    @Override
    public PostDTO createPost(PostDTO postDTO) {
        PostEntity postEntity = new PostEntity();
        postEntity.setTitle(postDTO.getTitle());
        postEntity.setDescription(postDTO.getDescription());
        postEntity.setCreatedAt(LocalDateTime.now());
        postEntity.setUpdatedAt(LocalDateTime.now());
        // Handle comments if any
        if (postDTO.getComments() != null) {
            List<CommentEntity> commentEntities = new ArrayList<>();
            for (CommentDTO commentDTO : postDTO.getComments()) {
                CommentEntity commentEntity = new CommentEntity();
                commentEntity.setPost(postEntity);
                commentEntity.setContent(commentDTO.getContent());
                commentEntity.setCreatedAt(LocalDateTime.now());
                commentEntities.add(commentEntity);
            }
            postEntity.setComments(commentEntities);
        }
        try {
            // Save the PostEntity and retrieve the saved entity
            PostEntity savedPostEntity = postRepository.save(postEntity);
            // Convert the saved entity to PostDTO
            return convertPostEntityToDTO(savedPostEntity);
        } catch (DataIntegrityViolationException ex) {
            logger.error("Failed to create post due to data integrity violation.", ex);
            throw ex;
        } catch (Exception ex) {
            logger.error("Failed to create post. Please try again later.", ex);
            throw new RuntimeException("Failed to create post. Please try again later.");
        }
    }

    @Override
    public PostEntity updatePost(Long id, PostDTO postDTO) {
        Optional<PostEntity> optionalPost = postRepository.findById(id);
        if (optionalPost.isPresent()) {
            PostEntity existingPost = optionalPost.get();
            existingPost.setTitle(postDTO.getTitle());
            existingPost.setDescription(postDTO.getDescription());
            existingPost.setUpdatedAt(LocalDateTime.now());
            List<CommentEntity> commentsToUpdate = new ArrayList<>();
            if (postDTO.getComments() != null) {
                for (CommentDTO commentDTO : postDTO.getComments()) {
                    CommentEntity commentEntity = new CommentEntity();
                    commentEntity.setPost(existingPost);
                    commentEntity.setContent(commentDTO.getContent());
                    commentEntity.setCreatedAt(LocalDateTime.now());
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

    @Override
    public void deletePost(Long id) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
        } else {
            logger.error("Post not found with id: {}", id);
            throw new CustomExceptions.PostNotFoundException(id);
        }
    }

    @Override
    public Optional<PostEntity> getPostById(Long id) {
        Optional<PostEntity> post = postRepository.findById(id);
        if (post.isPresent()) {
            return post;
        } else {
            logger.error("Post not found with id: {}", id);
            throw new CustomExceptions.PostNotFoundException(id);
        }
    }


    public PostDTO convertPostEntityToDTO(PostEntity postEntity) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(postEntity.getId());
        postDTO.setTitle(postEntity.getTitle());
        postDTO.setDescription(postEntity.getDescription());
        postDTO.setCreatedAt(postEntity.getCreatedAt());
        postDTO.setUpdatedAt(postEntity.getUpdatedAt());

        // Convert comments from Entity to DTO (assuming CommentConverter exists)
        List<CommentDTO> commentDtos = postEntity.getComments().stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
        postDTO.setComments(commentDtos);

        return postDTO;
    }

    public PostEntity convertPostDTOToEntity(PostDTO postDTO) {
        PostEntity postEntity = new PostEntity();
        postEntity.setId(postDTO.getId());
        postEntity.setTitle(postDTO.getTitle());
        postEntity.setDescription(postDTO.getDescription());
        postEntity.setCreatedAt(postDTO.getCreatedAt());
        postEntity.setUpdatedAt(postDTO.getUpdatedAt());

        // Convert comments from DTO to Entity (assuming CommentConverter exists)
        List<CommentEntity> comments = postDTO.getComments().stream()
                .map(this::convertDTOToEntity)
                .collect(Collectors.toList());
        postEntity.setComments(comments);

        return postEntity;
    }

    public CommentDTO convertEntityToDTO(CommentEntity commentEntity) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(commentEntity.getId());
        commentDTO.setContent(commentEntity.getContent());
        commentDTO.setCreatedAt(commentEntity.getCreatedAt());
        return commentDTO;
    }

    public CommentEntity convertDTOToEntity(CommentDTO commentDTO) {
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setId(commentDTO.getId());
        commentEntity.setContent(commentDTO.getContent());
        commentEntity.setCreatedAt(commentDTO.getCreatedAt());
        return commentEntity;
    }
}
