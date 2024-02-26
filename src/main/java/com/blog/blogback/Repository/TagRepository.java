package com.blog.blogback.Repository;
import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

import com.blog.blogback.Entity.Board;
import com.blog.blogback.Entity.Tag;

import jakarta.transaction.Transactional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long>{

    Optional<Tag> findByTagNo(Long tagNo);   //태그번호로 태그정보 불러오기

    @Transactional
    void deleteAllByBoard(Board board);
    
    @Query("SELECT t.tagName, COUNT(t.tagName) FROM Tag t GROUP BY t.tagName")
    List<Object[]> boardCountByTagName();

    @Query("SELECT COUNT(*) FROM Tag t")
    int allTagCnt();
}
