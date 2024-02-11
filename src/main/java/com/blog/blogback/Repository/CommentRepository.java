package com.blog.blogback.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blog.blogback.Entity.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{
    int countByBoardNo(Long boardNo);
    List<Comment> findByBoardNo(Long boardNo);
}
