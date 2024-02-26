package com.blog.blogback.Dto;

import java.util.List;
import com.blog.blogback.Entity.Board;
import com.blog.blogback.Entity.Tag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardWithTagDto {
    private Board board;
    private List<Tag> tags;
    private String tagName;
}
