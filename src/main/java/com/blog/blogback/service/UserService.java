package com.blog.blogback.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blog.blogback.Dto.BoardRequestDto;
import com.blog.blogback.Dto.JwtToken;
import com.blog.blogback.Dto.UserAuthDto;
import com.blog.blogback.Dto.UserDto;
import com.blog.blogback.Dto.UserJoinDto;
import com.blog.blogback.Dto.UserLoginDto;
import com.blog.blogback.Dto.UserRequestDto;
import com.blog.blogback.Entity.Authority;
import com.blog.blogback.Entity.Board;
import com.blog.blogback.Entity.Role;
import com.blog.blogback.Entity.User;
import com.blog.blogback.Entity.UserAuthority;
import com.blog.blogback.Repository.UserAuthRepository;
import com.blog.blogback.Repository.UserRepository;
import com.blog.blogback.common.JwtTokenProvider;
import com.blog.blogback.security.CustomUserDetails;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    private final UserRepository userRepository;
    //private final UserAuthRepository userAuthRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public UserDto signUp(UserJoinDto userJoinDto) {
        // 사용자 아이디로 이미 가입된 사용자가 있는지 확인
        //Optional<User> existingUser = userRepository.findByUserId(userJoinRequest.getUserId());
        Optional<User> existingUser = userRepository.findByUserId(userJoinDto.getUserId());
    
        // 이미 가입된 사용자가 있을 경우 예외 처리 또는 원하는 로직 수행
        if (existingUser.isPresent()) {
            throw new RuntimeException("이미 가입된 사용자입니다.");
        }

        String encodedPassword = passwordEncoder.encode(userJoinDto.getUserPw());
        String role = "ROLE_USER";
        return UserDto.toDto(userRepository.save(userJoinDto.toEntity(encodedPassword, role)));

        // 가입되지 않은 경우에는 새로운 사용자를 생성하여 저장
        // User newUser = User.builder()
        //         .userId(userRequestDto.getUserId())
        //         .userPw(passwordEncoder.encode(userRequestDto.getUserPw()))
        //         .name(userRequestDto.getName())
        //         .introduction(userRequestDto.getIntroduction())
        //         .build();
    
        //userRepository.save(newUser);   
    }

    @Transactional
    public JwtToken login(String username, String password){
        logger.debug("Attempting to authenticate user: {}", username);
        logger.debug("Attempting to authenticate user: {}", password);

        // userId와 userPw를  기반으로 Authentication 객체 생성,
        // 이떄 authentication은 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new 
        UsernamePasswordAuthenticationToken(username, password);
        System.out.println("authenticationToken : " + authenticationToken );

        // 실제 검증 authenticate() 매서드를 통해 요청된 User에 대한 검증 진행
        // authenticate 매서드가 실행될 때 CustomUserDetailsService에서 만든 loadUserByUsername 매서드 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        
        if(authentication.isAuthenticated()){
            logger.debug("Authentication succeeded for user: {}", username); 
        }else{
            logger.debug("Authentication failed for user: {}", username);
        }

        // 인증 정보를 기반으로 JWT 토큰 생성
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        System.out.println("jwtToken : " + jwtToken);
        return jwtToken;
    }

    @Transactional
    public void saveIntro(String introText, Long userNo) {
        // users 테이블의 introduction 컬럼에 intro 데이터를 저장하는 SQL 쿼리 실행
        String sql = "UPDATE users SET introduction = :introText WHERE userNo = :userNo";
        entityManager.createNativeQuery(sql)
                     .setParameter("introduction", introText)
                     .setParameter("userNo", userNo)
                     .executeUpdate();
    }

    // public Board updateIntro(BoardRequestDto boardDto) {
    //     Board entity = boardRepository.findById(boardDto.getBoardNo()).orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
    //     entity.setBoardTitle(boardDto.getBoardTitle());
    //     entity.setBoardContent(boardDto.getBoardContent());
    //     return boardRepository.save(entity);
    // }
    
}