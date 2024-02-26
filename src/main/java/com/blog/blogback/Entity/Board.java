package com.blog.blogback.Entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    @Column(name = "board_no")
    private Long boardNo;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "board_user_id", referencedColumnName = "userId")
    private User user;

    // @Column(length=255, nullable = false)
    // private String boardUserId;

    @Column(nullable = false)
    private String boardTitle;

    @Column(nullable = false)
    private String boardContent;

    @Column(length = 255)
    private String imgPath;
    //private String tag;
    @Column
    private LocalDateTime regDate;

    @Column
    private LocalDateTime updDate;

    // @Column(length = 100)
    // private String tag;

    @JsonIgnore
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Tag> tags;
    
    @Builder
    public Board(String boardUserId, String boardTitle, 
                    String boardContent, String imgPath, LocalDateTime regDate,
                    LocalDateTime updDate, List<Tag> tags) {
        Assert.hasText(boardUserId, "boardUserId must not be empty");
        Assert.hasText(boardTitle, "boardTitle must not be empty");
        Assert.hasText(boardContent, "boardContent must not be empty");

        //this.boardNo = boardNo;
        //this.boardUserId = boardUserId;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.imgPath = imgPath;
        this.regDate = LocalDateTime.now();
        this.updDate = updDate;

        if(tags != null){
          for(Tag tag : tags){
            tag.setBoard(this);
          }
          this.tags = tags;
        }
        //this.tag = tag;
  }

  @Override
  public String toString() {
      return "Board{" +
              "boardNo=" + boardNo +
              //", boardUserId='" + boardUserId + '\'' +
              ", boardTitle='" + boardTitle + '\'' +
              ", boardContent='" + boardContent + '\'' +
              ", imgPath='" + imgPath + '\'' +
              ", regDate=" + regDate +
              ", updDate=" + updDate +
              '}';
  }

public void addTag(Tag tag) {
    if (tags == null) {
        tags = new ArrayList<>();
    }
    tags.add(tag);
    tag.setBoard(this);
}

public void setBoardContent(String boardContent){
  this.boardContent = boardContent;
}

public void setBoardTitle(String boardTitle){
    this.boardTitle = boardTitle;
}

public void setUpdDate(LocalDateTime updDate){
  this.updDate = updDate;
}

}
