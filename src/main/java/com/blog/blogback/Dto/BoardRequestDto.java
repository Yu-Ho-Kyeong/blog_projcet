package com.blog.blogback.Dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

import com.blog.blogback.Entity.Board;
import com.blog.blogback.Entity.Tag;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardRequestDto {
    private Long boardNo;
    private String boardUserId;
    private String boardTitle;
    private String boardContent;
    private String imgPath;
    //private String tag;
    private List<Tag> tags;
    private LocalDateTime updDate;

    @Builder
    public BoardRequestDto(String boardUserId, String boardTitle, String boardContent, 
                            String imgPath, LocalDateTime updDate, List<Tag> tags) {
        //this.boardNo = boardNo;
        this.boardUserId = boardUserId;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.imgPath = imgPath;
        this.tags = tags;
        this.updDate = updDate;
    }

    // public BoardRequestDto(Board board) {
    //     this.boardNo = board.getBoardNo();
    //     this.boardUserId = board.getBoardUserId();
    //     this.boardTitle = board.getBoardTitle();
    //     this.boardContent = board.getBoardContent();
    //     this.imgPath = board.getImgPath();
    //     this.tags = board.getTags();
    // }

    public Board toEntity() {
        return Board.builder()
                .boardUserId(boardUserId)
                .boardTitle(boardTitle)
                .boardContent(boardContent)
                .imgPath(imgPath)
                .updDate(updDate)
                .tags(tags)
                .build();
    }

    public String toString() {
        return "BoardRequestDto{" +
                "boardNo=" + boardNo +
                ", boardUserId='" + boardUserId + '\'' +
                ", boardTitle='" + boardTitle + '\'' +
                ", boardContent='" + boardContent + '\'' +
                ", imgPath='" + imgPath + '\'' +
                //", regDate=" + regDate +
                ", updDate=" + updDate +
                '}';
    }

}
