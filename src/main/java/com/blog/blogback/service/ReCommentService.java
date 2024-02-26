package com.blog.blogback.service;

import org.springframework.stereotype.Service;

import com.blog.blogback.Repository.ReCommentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor    //final or NonNull 옵션 필드를 전부 포함한 생성자 만들어줌
@Service
public class ReCommentService {

    private final ReCommentRepository reCommentRepository;

    public int getReCommentCnt(Long commentNo){
        return reCommentRepository.countByCommentNo(commentNo);
    }
}
