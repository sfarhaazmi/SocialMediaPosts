package com.example.socialmediaposts.rest.response;

    public class CustomExceptions {

    public static class PostNotFoundException extends RuntimeException {
        public PostNotFoundException(Long id) {
            super("Post with ID " + id + " not found.");
        }
    }

    public static class CommentNotFoundException extends RuntimeException {
        public CommentNotFoundException(Long id) {
            super("Comment with ID " + id + " not found.");
        }
    }
}