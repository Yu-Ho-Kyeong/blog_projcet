package com.blog.blogback.Dto;

import java.util.ArrayList;
import java.util.List;

import com.blog.blogback.Entity.Role;
import com.blog.blogback.Entity.User;
import com.blog.blogback.Entity.UserAuthority;
import com.blog.blogback.security.CustomUserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserJoinDto {
    private String userId;
    private String userPw; 
    private String name;
    private String introduction;
    private String imgPath;
    private String role;

    public User toEntity(String encodedPassword, String role) {
        
        return User.builder()
            .userId(userId)
            .userPw(encodedPassword)
            .role(role)
            .name(name)
            .introduction(introduction)
            .imgPath(imgPath)
            .build();
    }

}
