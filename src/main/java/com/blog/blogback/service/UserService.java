package com.blog.blogback.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blog.blogback.Dto.UserRequestDto;
import com.blog.blogback.Entity.User;
import com.blog.blogback.Repository.UserRepository;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void signup(UserRequestDto userJoinRequest) {
        // 사용자 아이디로 이미 가입된 사용자가 있는지 확인
        User existingUser = userRepository.findByUserId(userJoinRequest.getUserId());
    
        // 이미 가입된 사용자가 있을 경우 예외 처리 또는 원하는 로직 수행
        if (existingUser != null) {
            throw new RuntimeException("이미 가입된 사용자입니다.");
        }
    
        // 가입되지 않은 경우에는 새로운 사용자를 생성하여 저장
        User newUser = User.builder()
                .userId(userJoinRequest.getUserId())
                .userPw(userJoinRequest.getUserPw())
                .name(userJoinRequest.getName())
                .introduction(userJoinRequest.getIntroduction())
                .build();
    
        userRepository.save(newUser);
    }
}