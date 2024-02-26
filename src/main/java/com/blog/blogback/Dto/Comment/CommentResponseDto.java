package com.blog.blogback.Dto.Comment;

import lombok.Getter;

import java.time.LocalDateTime;

import com.blog.blogback.Entity.Comment;

@Getter

public class CommentResponseDto {
    private Long commentNo;         // 댓글 번호
    private Long boardNo;           // 게시글 번호
    private String commentContent;  // 댓글 내용
    private String commentUserId;   // 댓글 id
    private LocalDateTime regDate;  // 등록일
   
    public CommentResponseDto(Comment comment){
      this.commentNo = comment.getCommentNo();
      this.boardNo = comment.getBoardNo();
      this.commentContent = comment.getCommentContent();
      this.commentUserId = comment.getCommentUserId();
      this.regDate = comment.getRegDate();
    }

    @Override
    public String toString() {
      return "Comment{" +
              //"commentNo=" + commentNo +
              ", boardNo='" + boardNo + '\'' +
              ", commentContent='" + commentContent + '\'' +
              ", commentUserId=" + commentUserId +
              ", regDate=" + regDate +
              '}';
  }
}
