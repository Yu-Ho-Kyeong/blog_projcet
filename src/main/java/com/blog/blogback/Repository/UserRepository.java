package com.blog.blogback.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blog.blogback.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
    User findByUserIdAndUserPw(String userId, String userPw);
    User findByUserId(String userId);
}

