package com.blog.blogback.Dto;

import com.blog.blogback.Entity.Authority;
import com.blog.blogback.Entity.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserAuthDto {
    private Role authName;

    public Authority toEntity(Role authName) {
        return Authority.builder()
            .authName(authName)
            .build();
    }
}
