package com.blog.blogback.Dto.Comment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import com.blog.blogback.Entity.Comment;

@Getter
@NoArgsConstructor()
public class CommentSaveDto {
    private Long boardNo;           // 게시글 번호
    private String commentContent;  // 댓글 내용
    private String commentUserId;   // 댓글 id
    private LocalDateTime regDate;  // 등록일

    @Builder
    public CommentSaveDto(Long boardNo, String content, String userId, LocalDateTime regDate) {
        this.boardNo = boardNo;
        this.commentContent = content;
        this.commentUserId = userId;
        this.regDate = LocalDateTime.now();
    }

    //resquest dto 로 받은 Posts 객체를 entity 화하여 저장하는 용도
    public Comment toEntity() {
        return Comment.builder()
            .boardNo(boardNo)
            .commentContent(commentContent)
            .commentUserId(commentUserId)
            .regDate(regDate)
            .build();
    }
}
