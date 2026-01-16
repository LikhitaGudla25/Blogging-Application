package com.blogapp.service;

import java.util.List;

import com.blogapp.entity.Post;

public interface PostService {

    public Post createPost(Post post);

    public List<Post> getAllPosts();

    public List<Post> getPostsByUserId(Integer userId);

    public Post getPostById(Integer id);

    List<Post> getPublishedPosts();

    public void deletePost(Integer id);

    public List<Post> searchPosts(String query);
}
