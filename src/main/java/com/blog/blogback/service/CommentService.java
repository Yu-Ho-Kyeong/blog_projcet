package com.blog.blogback.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.blog.blogback.Dto.CommentRequestDto;
import com.blog.blogback.Entity.Board;
import com.blog.blogback.Entity.Comment;
import com.blog.blogback.Repository.CommentRepository;

@Service
public class CommentService {
    @Autowired private CommentRepository commentRepository;

    // 댓글 count
    public int getCommentCountByBoardNo(Long boardNo) {
        return commentRepository.countByBoardNo(boardNo);
    }
    // 댓글 작성
    public Long addComment(CommentRequestDto commentRequestDto) {
        Comment entity = commentRequestDto.toEntity();
        commentRepository.save(entity);
        return entity.getCommentNo();
    }
}
