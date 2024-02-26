package com.blog.blogback.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


@Table(name="authes")    // users table과 Users class와 맵핑
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authority {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authNo;

    //@Column(name="auth", nullable = false)

    @Enumerated(EnumType.STRING)
    private Role authName;

    @OneToMany(mappedBy = "auth", fetch = FetchType.EAGER)
    private List<UserAuthority> roles = new ArrayList<>();
    
    @Builder
    public Authority(Role authName){
        this.authName = authName;
    }

    public String getRoleKey() {
        return authName.getRoleKey();
    }
    
}
