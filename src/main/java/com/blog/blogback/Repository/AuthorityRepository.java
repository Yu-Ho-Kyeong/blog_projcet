package com.blog.blogback.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blog.blogback.Entity.Authority;
import com.blog.blogback.Entity.Role;
import com.blog.blogback.Entity.User;

@Repository
public interface AuthorityRepository extends JpaRepository<User, Long>{
    Optional<Authority> findByName(Role role);
}
