package com.blog.blogback.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.util.Assert;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="Comment")
@Entity

public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentNo;         // 댓글 번호

    @Column
    private Long boardNo;           // 게시글 번호

    @Column(nullable = false)
    private String commentContent;  // 댓글 내용

    @Column(length=255, nullable = false)
    private String commentUserId;   // 댓글 id

    @Column
    private LocalDateTime regDate;  // 등록일


    @Builder
    public Comment(Long commentNo, Long boardNo, String commentContent, String commentUserId, 
                    LocalDateTime regDate) {
        Assert.hasText(commentContent, "boardUserId must not be empty");
        Assert.hasText(commentUserId, "boardTitle must not be empty");

        this.commentNo = commentNo;
        this.boardNo = boardNo;
        this.commentContent = commentContent;
        this.commentUserId = commentUserId;
        this.regDate = LocalDateTime.now();
  }

}
