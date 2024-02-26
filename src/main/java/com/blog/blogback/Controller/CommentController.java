package com.blog.blogback.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.blog.blogback.Repository.BoardRepository;
import com.blog.blogback.Repository.CommentRepository;
import com.blog.blogback.service.BoardService;
import com.blog.blogback.service.CommentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.blog.blogback.Dto.Comment.CommentResponseDto;
import com.blog.blogback.Dto.Comment.CommentSaveDto;
import com.blog.blogback.Dto.Comment.CommentUpdateDto;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/user/comment")
@RestController
public class CommentController {

    //@Autowired private CommentRepository commentRepository;
    //@Autowired private CommentService commentService;

    //private final CommentRepository commentRepository;
    private final CommentService commentService;

    // 게시물 댓글 count
    @GetMapping("/getCount/{boardNo}")
    public int getCount(@PathVariable Long boardNo) {
        return commentService.getCommentCountByBoardNo(boardNo);
    }

    @GetMapping("/getComment/{boardNo}")
    public List<CommentResponseDto> getComment(@PathVariable Long boardNo) {
        return commentService.findByBoardNo(boardNo);
    }

    // 댓글 작성
    @PostMapping("/addComment")
    public Long saveComment(@RequestBody CommentSaveDto saveDto) {
        return commentService.addComment(saveDto);  
    }

    // 댓글 수정
    @PutMapping("/modifyComment/{commentNo}")
    public Long modifyComment(@PathVariable Long commentNo, @RequestBody CommentUpdateDto updateDto) {
        log.info("modifyComment() : {}", commentNo);
        return commentService.modifyComment(commentNo, updateDto);
    }

    // 댓글 삭제
    @DeleteMapping("/deleteComment/{commentNo}")
    public void deleteComment(@PathVariable Long commentNo){
        commentService.deleteComment(commentNo);
    }

    // 댓글번호로 댓글정보 조회
    @GetMapping("/findByCommentNo/{commentNo}")
    public CommentResponseDto getFindByCommentNo(@PathVariable Long commentNo) {
        return commentService.findByCommentNo(commentNo);
    }
}
