package com.blog.blogback.Dto.ReComment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor()
public class ReCommentUpdateDto {
    private Long reCommentNo;         // 댓글 번호
    private String reCommentContent;  // 댓글 내용

    @Builder
    public ReCommentUpdateDto(Long reCommentNo, String reCommentContent) {
        this.reCommentNo = reCommentNo;
        this.reCommentContent = reCommentContent;
    }
}
