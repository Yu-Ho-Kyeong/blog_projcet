package com.blog.blogback.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {

    ROLE_ADMIN("ROLE_ADMIN", "관리자", "10"),
    ROLE_USER("ROLE_USER", "일반", "20"),
    ;

    private String roleKey;
    private String roleName;
    private String roleValue;

}
