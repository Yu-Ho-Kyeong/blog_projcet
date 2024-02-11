package com.blog.blogback.Dto;

import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import com.blog.blogback.Entity.Board;
import com.blog.blogback.Entity.User;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardRequestDto {
    private Long boardNo;
    private String boardUserId;
    private String boardTitle;
    private String boardContent;
    private String imgPath;
    private String tag;

    @Builder
    public BoardRequestDto(Long boardNo, String boardUserId, String boardTitle, 
            String boardContent, String imgPath, String tag) {
        this.boardNo = boardNo;
        this.boardUserId = boardUserId;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.imgPath = imgPath;
        this.tag = tag;
    }

    public BoardRequestDto(Board board) {
        this.boardNo = board.getBoardNo();
        this.boardUserId = board.getBoardUserId();
        this.boardTitle = board.getBoardTitle();
        this.boardContent = board.getBoardContent();
        this.imgPath = board.getImgPath();
        this.tag = board.getTag();
    }

    public Board toEntity() {
        return Board.builder()
                .boardNo(boardNo)
                .boardUserId(boardUserId)
                .boardTitle(boardTitle)
                .boardContent(boardContent)
                .imgPath(imgPath)
                .tag(tag)
                .build();
    }
}
