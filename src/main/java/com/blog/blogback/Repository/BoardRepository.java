package com.blog.blogback.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.blog.blogback.Entity.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>{
    Optional<Board> findByBoardNo(Long boardNo);   //게시글 번호로 게시글 불러오기
}
