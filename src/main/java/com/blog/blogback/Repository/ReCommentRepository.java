package com.blog.blogback.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blog.blogback.Entity.ReComment;

@Repository
public interface ReCommentRepository extends JpaRepository<ReComment, Long>{
    int countByCommentNo(Long commentNo);
}
