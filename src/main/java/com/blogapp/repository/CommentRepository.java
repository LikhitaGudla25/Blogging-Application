package com.blogapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogapp.entity.Comment;
import com.blogapp.entity.Post;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    public List<Comment> findByPost(Post post);

    List<Comment> findCommentsByPostId(Integer postId);
}
