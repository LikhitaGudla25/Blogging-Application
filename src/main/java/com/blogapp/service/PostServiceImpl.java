package com.blogapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blogapp.entity.Post;
import com.blogapp.repository.PostRepository;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Override
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> getPublishedPosts() {
        return postRepository.findByStatus("PUBLISHED");
    }

    @Override
    public List<Post> getPostsByUserId(Integer userId) {
        return postRepository.findByUserId(userId);
    }

    @Override
    public Post getPostById(Integer id) {
        return postRepository.findById(id).orElse(null);
    }

    @Override
    public void deletePost(Integer id) {
        postRepository.deleteById(id);
    }

    @Override
    public List<Post> searchPosts(String query) {
        return postRepository.findByTitleContainingOrContentContaining(query, query);
    }
}
