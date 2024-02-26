package com.blog.blogback.Controller;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.blogback.Repository.CommentRepository;
import com.blog.blogback.Repository.ReCommentRepository;
import com.blog.blogback.service.ReCommentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/recomment")
@RequiredArgsConstructor    //final or NonNull 옵션 필드를 전부 포함한 생성자 만들어줌
@RestController
public class ReCommentController {
    private final ReCommentService reCommentService;

    @GetMapping("/getCount/{commentNo}")
    public int getCount(@PathVariable Long commentNo){
        log.info("commentNo : {}" , commentNo);
        return reCommentService.getReCommentCnt(commentNo);
    }
}
