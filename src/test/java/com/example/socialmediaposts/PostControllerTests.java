package com.example.socialmediaposts;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PostControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Test get all posts")
    public void testGetAllPosts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/posts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    @DisplayName("Test create post")
    public void testCreatePost() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\": \"New Post\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.description").value("New Post"));
    }

    @Test
    @DisplayName("Test update post")
    public void testUpdatePost() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\": \"Updated Post\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Updated Post"));

        mockMvc.perform(MockMvcRequestBuilders.put("/posts/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\": \"Updated Post\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test delete post")
    public void testDeletePost() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/posts/1"))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.delete("/posts/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test get post by ID")
    public void testGetPostById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        mockMvc.perform(MockMvcRequestBuilders.get("/posts/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
