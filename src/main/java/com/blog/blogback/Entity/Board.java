package com.blog.blogback.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.util.Assert;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="board")
public class Board {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardNo;

    // @Column
    // private Long categoryNo;

    @Column(length=255, nullable = false)
    private String boardUserId;

    @Column(nullable = false)
    private String boardTitle;

    @Column(nullable = false)
    private String boardContent;

    @Column(length = 255)
    private String imgPath;

    @Column
    private LocalDateTime regDate;

    @Column
    private LocalDateTime updDate;

    @Column(length = 100)
    private String tag;

    @Builder
    public Board(Long boardNo, String boardUserId, String boardTitle, 
                    String boardContent, String imgPath, LocalDateTime regDate,
                    LocalDateTime updDate, String tag) {
        Assert.hasText(boardUserId, "boardUserId must not be empty");
        Assert.hasText(boardTitle, "boardTitle must not be empty");
        Assert.hasText(boardContent, "boardContent must not be empty");

        this.boardNo = boardNo;
        this.boardUserId = boardUserId;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.imgPath = imgPath;
        this.regDate = LocalDateTime.now();
        this.tag = tag;
  }

  public void setBoardTitle(String boardTitle) {
    this.boardTitle = boardTitle;
  }

  public void setBoardContent(String boardContent) {
    this.boardContent = boardContent;
  }
}
