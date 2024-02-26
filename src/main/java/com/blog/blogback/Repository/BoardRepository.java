package com.blog.blogback.Repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.blog.blogback.Entity.Board;

import java.util.List;
import java.util.Map;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>{
    //@Query("SELECT b, u.introduction FROM User u JOIN Board b ON u.userId = b.boardUserId WHERE b.boardNo = :boardNo")
    Optional<Board> findByBoardNo(Long boardNo);   //게시글 번호로 게시글 불러오기

    @Query("SELECT MAX(b.boardNo) AS preBoardNo, b.boardTitle AS boardTitle FROM Board b WHERE b.boardNo < :boardNo")
    Map<String, Object> findPreByBoardNo(Long boardNo);   //게시글 번호로 게시글 불러오기

    @Query("SELECT MIN(b.boardNo) AS nextBoardNo, b.boardTitle AS boardTitle FROM Board b WHERE b.boardNo > :boardNo")
    Map<String, Object> findNextByBoardNo(Long boardNo);   //게시글 번호로 게시글 불러오기


    // Long findNextByBoardNo(Long boardNo);   //게시글 번호로 게시글 불러오기


    // 이전 게시글 번호 찾기
   // List<Board> findFirstByBoardNoLessThanOrderByBoardNoDesc(Long boardNo, Pageable pageable);

    // 다음 게시글 번호 찾기
    //List<Board> findFirstByBoardNoGreaterThanOrderByBoardNoAsc(Long boardNo, Pageable pageable);
 
    @Query("SELECT MAX(b.boardNo) FROM Board b")
    Long findMaxBoardNo();

    @Query("SELECT MIN(b.boardNo) FROM Board b")
    Long findMinBoardNo();




    //@Query("SELECT b FROM Board b JOIN FETCH b.tags")
    @Query("SELECT b, CONCAT(t.tagName, ',') FROM Board b JOIN b.tags t GROUP BY b")
    List<Board> findAllWithTags(Sort sort);

    @Query("SELECT b, t.tagName FROM Board b JOIN b.tags t WHERE t.tagName = :tagName GROUP BY b")
    List<Board> findBoardWithTags(String tagName, Sort sort);
    
    
}
