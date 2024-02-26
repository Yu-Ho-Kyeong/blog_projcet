package com.blog.blogback.Entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


// import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;


@Table(name="users") 
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userNo;
    @Column(name="userId", nullable = false, unique = true)
    private String userId;
    @Column(name="userPw", nullable = false)
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
    @Column(name = "role")
    private String role;

    @Builder
    public User(Long userNo, String userId, String userPw, String role, String name, 
                String introduction, String imgPath, LocalDateTime regDate, char enabled) 
    {
        Assert.hasText(userId, "userId must not be empty");
        Assert.hasText(userPw, "userPw must not be empty");
        Assert.hasText(name, "name must not be empty");

        this.userId = userId;
        this.userPw = userPw;
        this.role = role;
        this.name = name;
        this.introduction = introduction;
        this.imgPath = imgPath;
        this.regDate = LocalDateTime.now();
        this.enabled = '1';
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
      }
    
    /**
    * 
    * @description 패스워드 저장시 자동 암호화
    */
//    public User encodePassword(PasswordEncoder passwordEncoder) {
//       this.userPw = passwordEncoder.encode(this.userPw);
//       return this;
//    }


}
