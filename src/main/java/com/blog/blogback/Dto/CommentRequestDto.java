package com.blog.blogback.Dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

import com.blog.blogback.Entity.Board;
import com.blog.blogback.Entity.Comment;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentRequestDto {
    private Long commentNo;         // 댓글 번호
    private Long boardNo;           // 게시글 번호
    private String commentContent;  // 댓글 내용
    private String commentUserId;   // 댓글 id
    private LocalDateTime regDate;  // 등록일

    @Builder
    public CommentRequestDto(Long commentNo, Long boardNo, String commentContent, 
                             String commentUserId) {

        this.commentNo = commentNo;
        this.boardNo = boardNo;
        this.commentContent = commentContent;
        this.commentUserId = commentUserId;

    }

    public CommentRequestDto(Comment comment) {
        this.commentNo = comment.getCommentNo();
        this.boardNo = comment.getBoardNo();
        this.commentContent = comment.getCommentContent();
        this.commentUserId = comment.getCommentUserId();
    }

    public Comment toEntity() {
        return Comment.builder()
                .commentNo(commentNo)
                .boardNo(boardNo)
                .commentContent(commentContent)
                .commentUserId(commentUserId)
                .build();
    }
}
