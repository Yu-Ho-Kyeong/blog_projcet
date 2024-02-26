package com.blog.blogback.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.blog.blogback.Dto.BoardRequestDto;
import com.blog.blogback.Dto.BoardWithTagDto;
import com.blog.blogback.Dto.TagRequestDto;
import com.blog.blogback.Entity.Board;
import com.blog.blogback.Entity.Tag;
import com.blog.blogback.Repository.BoardRepository;
import com.blog.blogback.Repository.TagRepository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor    //final or NonNull 옵션 필드를 전부 포함한 생성자 만들어줌
@Service
public class BoardService {
    
    //@Autowired 사용 지양됨 -> @RequiredArgsConstructor 로 생성되는 생성자로 주입받기 위해 final 붙임.
    //@Autowired private BoardRepository boardRepository;
    //@Autowired private TagRepository tagRepository;

    private final BoardRepository boardRepository;
    private final TagRepository tagRepository;

    public Long saveBoard(BoardRequestDto boardRequestDto) {
        log.info("saveBoard() 진입");
        // 게시글 정보를 Board 엔티티로 변환
        log.info("Before conversion to board: {}", boardRequestDto.toString());
        Board board = boardRepository.save(boardRequestDto.toEntity());
        
        //List<Tag> tags = boardRequestDto.getTags();
        //log.info("conversion to tags: {}", tags.toString());
     
        return board.getBoardNo();
    }
    public Board update(BoardRequestDto boardRequestDto) {
        // 현재 시간 가져오기
        LocalDateTime now = LocalDateTime.now();
    
        // 받아온 태그 목록
        List<Tag> tags = boardRequestDto.getTags();
    
        // Board 엔티티를 수정하기 위해 필요한 데이터 설정
        Board board = boardRepository.findById(boardRequestDto.getBoardNo())
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

                board.setBoardTitle(boardRequestDto.getBoardTitle());
                board.setBoardContent(boardRequestDto.getBoardContent());
                board.setUpdDate(now);
    
        // 태그 엔티티를 갱신
        board.getTags().clear(); // 기존 태그 삭제
        if (tags != null) {
            for (Tag tag : tags) {
                board.addTag(tag); // 새로운 태그 추가
            }
        }
    
        // 수정된 Board 엔티티 저장
        return boardRepository.save(board);
    }
    public void delete(Long boardNo) {
        Board entity = boardRepository.findById(boardNo).orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        boardRepository.delete(entity);
    }

    public List<Object[]> getTag(){
        List<Object[]> tagInfo = tagRepository.boardCountByTagName();
        return tagInfo;
    }

    public List<Board> findBoardWithTags(String tagName) {
        Sort sort = Sort.by(Sort.Direction.DESC, "regDate");
        return boardRepository.findBoardWithTags(tagName, sort);
    }

    public int allTagCnt(){
        int cnt = tagRepository.allTagCnt();
        return cnt;
    }


    public Map<String, Object> findPreBoard(Long boardNo) {
        return boardRepository.findPreByBoardNo(boardNo);
    }

    public Map<String, Object> findNextBoard(Long boardNo) {
        return boardRepository.findNextByBoardNo(boardNo);
    }

    public Long findMaxBoardNo() {
        return boardRepository.findMaxBoardNo();
    }

    public Long findMinBoardNo() {
        return boardRepository.findMinBoardNo();
    }



}