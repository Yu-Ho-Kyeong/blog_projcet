package com.blog.blogback.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.blogback.Dto.BoardRequestDto;
import com.blog.blogback.Entity.Board;
import com.blog.blogback.Repository.BoardRepository;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    public Long saveBoard(BoardRequestDto boardRequestDto) {
        Board entity = boardRequestDto.toEntity();
        boardRepository.save(entity);
        return entity.getBoardNo();
    }

    public Board update(BoardRequestDto boardDto) {
        Board entity = boardRepository.findById(boardDto.getBoardNo()).orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        entity.setBoardTitle(boardDto.getBoardTitle());
        entity.setBoardContent(boardDto.getBoardContent());
        return boardRepository.save(entity);
    }

    public void delete(Long boardNo) {
        Board entity = boardRepository.findById(boardNo).orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        boardRepository.delete(entity);
    }
}