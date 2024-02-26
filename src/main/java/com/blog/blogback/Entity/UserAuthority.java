package com.blog.blogback.Entity;

import java.util.Set;

import org.springframework.util.Assert;

import com.blog.blogback.security.CustomUserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name="user_auth")    // users table과 Users class와 맵핑
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class UserAuthority {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="userNo")
    private CustomUserDetails user;

    @ManyToOne
    @JoinColumn(name="authNo")
    private Authority auth;

    @Builder
    public UserAuthority(CustomUserDetails user, Authority auth){
        this.user = user;
        this.auth = auth;
    }

}
