package com.blog.blogback.Dto.ReComment;

import com.blog.blogback.Entity.Comment;
import com.blog.blogback.Entity.ReComment;

import lombok.Builder;

import java.time.LocalDateTime;

public class ReCommentSaveDto {
    private Long commentNo;         // 댓글 번호
    private Long boardNo;           // 게시글 번호
    private String commentContent;  // 댓글 내용
    private String commentUserId;   // 댓글 id
    private LocalDateTime regDate;  // 등록일

    @Builder
    public ReCommentSaveDto(Long commentNo, Long boardNo, String content, 
                    String userId, LocalDateTime regDate) {

        this.commentNo = commentNo;
        this.boardNo = boardNo;
        this.commentContent = content;
        this.commentUserId = userId;
        this.regDate = LocalDateTime.now();
    }

    //resquest dto 로 받은 Posts 객체를 entity 화하여 저장하는 용도
    public ReComment toEntity() {
        return ReComment.builder()
            .commentNo(commentNo)
            .boardNo(boardNo)
            .reCommentContent(commentContent)
            .reCommentUserId(commentUserId)
            .regDate(regDate)
            .build();
    }
}
