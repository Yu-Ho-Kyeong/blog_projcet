package com.blog.blogback.Dto.ReComment;

import lombok.Getter;

import java.time.LocalDateTime;

import com.blog.blogback.Entity.Comment;
import com.blog.blogback.Entity.ReComment;

@Getter

public class ReCommentResponseDto {
    private Long reCommentNo;           // 대댓글 번호
    private Long boardNo;               // 게시글 번호
    private Long commentNo;             // 댓글 번호
    private String reCommentContent;    // 댓글 내용
    private String reCommentUserId;     // 댓글 id
    private LocalDateTime regDate;      // 등록일

    public ReCommentResponseDto(ReComment reComment){
        this.reCommentNo = reComment.getReCommentNo();
        this.commentNo = reComment.getCommentNo();
        this.boardNo = reComment.getBoardNo();
        this.reCommentContent = reComment.getReCommentContent();
        this.reCommentUserId = reComment.getReCommentUserId();
        this.regDate = reComment.getRegDate();
    }
}
