package com.example.socialmediaposts;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getPosts() throws Exception {
        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    public void createPost() throws Exception {
        mockMvc.perform(post("/posts")
                        .contentType("application/json")
                        .content("{\"description\": \"New Post\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    public void updatePost() throws Exception {
        mockMvc.perform(put("/posts/1")
                        .contentType("application/json")
                        .content("{\"description\": \"Updated Post\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void deletePost() throws Exception {
        mockMvc.perform(delete("/posts/1"))
                .andExpect(status().isNoContent());
    }
}
