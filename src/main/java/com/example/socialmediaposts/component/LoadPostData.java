package com.example.socialmediaposts.component;

import com.example.socialmediaposts.repository.CommentEntity;
import com.example.socialmediaposts.repository.ISpringDataPostRepository;
import com.example.socialmediaposts.repository.PostEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class LoadPostData implements CommandLineRunner {
    @Autowired
    private ISpringDataPostRepository postRepository;

    @Override
    public void run(String... args) {
        for (int i = 1; i <= 10; i++) {
            PostEntity post = new PostEntity();
            post.setTitle("Post Title " + i);
            post.setDescription("This is the description for post number " + i);
            post.setCreatedAt(LocalDateTime.now());
            post.setUpdatedAt(LocalDateTime.now());

            List<CommentEntity> comments = new ArrayList<>();
            for (int j = 1; j <= 1; j++) {
                CommentEntity comment = new CommentEntity();
                comment.setContent("This is comment " + j + " for post number " + i);
                comment.setCreatedAt(LocalDateTime.now());
                comment.setPost(post);
                comments.add(comment);
            }
            post.setComments(comments);

            postRepository.save(post);
        }
    }
}
