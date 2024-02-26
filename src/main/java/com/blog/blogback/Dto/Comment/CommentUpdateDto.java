package com.blog.blogback.Dto.Comment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor()
public class CommentUpdateDto {
    private Long commentNo;         // 댓글 번호
    private String commentContent;  // 댓글 내용

    @Builder
    public CommentUpdateDto(Long commentNo, String content) {
        this.commentNo = commentNo;
        this.commentContent = content;
    }
}
