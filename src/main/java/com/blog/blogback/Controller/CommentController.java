package com.blog.blogback.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.blog.blogback.Repository.BoardRepository;
import com.blog.blogback.Repository.CommentRepository;
import com.blog.blogback.service.BoardService;
import com.blog.blogback.service.CommentService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.blog.blogback.Dto.CommentRequestDto;
import com.blog.blogback.Entity.Board;
import com.blog.blogback.Entity.Comment;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api/user/comment")
@RestController
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    // 게시물 댓글 count
    @GetMapping("/getCount/{boardNo}")
    public int getCount(@RequestParam Long boardNo) {
        int count = commentRepository.countByBoardNo(boardNo);
        return count;
    }

    @GetMapping("/getComment/{boardNo}")
    public ResponseEntity<?> getComment(@PathVariable Long boardNo) {
        try {
            List<Comment> comments = commentRepository.findByBoardNo(boardNo);
            return new ResponseEntity<>(comments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 댓글 작성
    @PostMapping("/addComment")
    public ResponseEntity<String> saveComment(@RequestBody CommentRequestDto commentRequestDto) {
        try {
            commentService.addComment(commentRequestDto);
            return ResponseEntity.ok("댓글이 성공적으로 작성되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 작성 중 오류가 발생했습니다.");
        }
    }

}
