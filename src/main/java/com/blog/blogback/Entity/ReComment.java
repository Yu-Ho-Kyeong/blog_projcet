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
@Table(name="recomment")
@Entity
public class ReComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reCommentNo;           // 대댓글 번호
    @Column
    private Long boardNo;               // 게시글 번호
    @Column
    private Long commentNo;             // 댓글 번호
    @Column(nullable = false)
    private String reCommentContent;    // 대댓글 내용
    @Column
    private String reCommentUserId;     // 대댓글 작성자
    @Column
    private LocalDateTime regDate;      // 등록일

    @Builder
    public ReComment(Long reCommentNo, Long commentNo, Long boardNo, 
                String reCommentContent, String reCommentUserId, LocalDateTime regDate) {
        Assert.hasText(reCommentContent, "boardUserId must not be empty");
        Assert.hasText(reCommentUserId, "boardTitle must not be empty");
        
        this.reCommentNo = reCommentNo;
        this.reCommentNo = reCommentNo;
        this.boardNo = boardNo;
        this.reCommentContent = reCommentContent;
        this.reCommentUserId = reCommentUserId;
        this.regDate = LocalDateTime.now();
    }

    @Override
    public String toString() {
      return "Comment{" +
                "reCommentNo=" + reCommentNo +
                "commentNo=" + commentNo +
                ", boardNo='" + boardNo + '\'' +
                ", commentContent='" + reCommentContent + '\'' +
                ", commentUserId=" + reCommentUserId +
                ", regDate=" + regDate +
              '}';
  }

  public void update(String content){
    this.reCommentContent = content;

  }
}
