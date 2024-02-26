package com.blog.blogback.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.blogback.Entity.Authority;


public interface UserAuthRepository extends JpaRepository<Authority, Long>{
    
}
