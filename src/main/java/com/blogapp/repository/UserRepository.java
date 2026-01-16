package com.blogapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogapp.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    public List<User> findByEmailAndPwd(String email, String pwd);

    public List<User> findByEmail(String email);
}
