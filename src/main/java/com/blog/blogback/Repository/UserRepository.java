package com.blog.blogback.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blog.blogback.Entity.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    User findByUserIdAndUserPw(String userId, String userPw);
    //Optional<CustomUserDetails> findByUsername(String username);
    Optional<User> findByUserId(String userId);
    Boolean existsByUserId(String userId);
}

