package com.example.socialmediaposts.rest;

import com.example.socialmediaposts.model.PostDTO;
import com.example.socialmediaposts.repository.CommentEntity;
import com.example.socialmediaposts.repository.PostEntity;
import com.example.socialmediaposts.service.CommentService;
import com.example.socialmediaposts.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.example.socialmediaposts.rest.response.CustomExceptions.PostNotFoundException;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
@Tag(name = "Post Controller", description = "Provides Post CRUD APIs")
public class PostController {

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);
    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;

    @GetMapping
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Retrieved all posts"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Request is invalid. Please check the request body and/or parameters."),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied")
            })
    public ResponseEntity<Page<PostEntity>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<PostEntity> posts = postService.getAllPosts(pageable);
        if (!posts.isEmpty()) {
            logger.info("Retrieved all posts successfully");
            return ResponseEntity.ok(posts);
        } else {
            logger.warn("No posts found");
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    @Operation(summary = "Create a new post")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Post created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<PostEntity> createPost(@RequestBody PostEntity post) {
        logger.info("Post created successfully");
        return new ResponseEntity<>(postService.createPost(post), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(summary = "Update a post")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Post updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    public ResponseEntity<Object> updatePost(@PathVariable Long id, @RequestBody PostDTO postDTO) {
        PostEntity updatedPost = postService.updatePost(id, postDTO);
        logger.info("Post updated successfully");
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a post")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Post deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    public ResponseEntity<Object> deletePost(@PathVariable Long id) {
        postService.deletePost(id);  // This method should throw PostNotFoundException if post is not found
        logger.info("Post deleted successfully with id: {}", id);
        return ResponseEntity.status(HttpStatus.OK).body("Post deleted successfully");
    }

    @GetMapping("/{postId}/comments")
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(summary = "Retrieve comments for a post")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Retrieved comments successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    public ResponseEntity<Page<CommentEntity>> getComments(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (postService.getPostById(postId).isEmpty()) {
            logger.error("Post not found with id: {}", postId);
            throw new PostNotFoundException(postId);
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<CommentEntity> comments = commentService.getComments(postId, pageable);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(summary = "Retrieve a post by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Post retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    public ResponseEntity<Object> getPostById(@PathVariable Long id) {
        Optional<PostEntity> post = postService.getPostById(id);
        if (post.isPresent()) {
            logger.info("Retrieved post successfully with id: {}", id);
            return new ResponseEntity<>(post.get(), HttpStatus.OK);
        }
        return null;
    }
}

