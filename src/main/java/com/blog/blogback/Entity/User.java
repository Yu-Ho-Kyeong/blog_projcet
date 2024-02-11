package com.blog.blogback.Entity;

import java.time.LocalDateTime;

import org.springframework.util.Assert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;


@Table(name="users")    // users table과 Users class와 맵핑
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userNo;
    @Column(length=100, nullable = false, unique = true)
    private String userId;
    @Column(length = 100, nullable = false)
    private String userPw; 
    @Column
    private String name;
    @Column
    private String introduction;
    @Column
    private LocalDateTime regDate;
    @Column
    private LocalDateTime updDate;
    @Column
    private String imgPath;
    @Column(nullable = false)
    private char enabled;

    @Builder
    public User(Integer userNo, String userId, String userPw, String name, 
                String introduction, String imgPath, char enabled) 
    {
        Assert.hasText(userId, "userId must not be empty");
        Assert.hasText(userPw, "userPw must not be empty");
        Assert.hasText(name, "name must not be empty");

        this.userId = userId;
        this.userPw = userPw;
        this.name = name;
        this.introduction = introduction;
        this.imgPath = imgPath;
        this.enabled = '1';
    }
}
