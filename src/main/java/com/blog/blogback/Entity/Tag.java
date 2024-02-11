package com.blog.blogback.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;

@Table(name="tag")
@Entity
@Getter
public class Tag {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagNo;
    
    @Column
    private Long boardNo;
    
    @Column(length = 100)
    private String tagName;

    @Column
    private LocalDateTime regDate;

    /* @OneToOne(mappedBy = "locker")
    private Board board; */
}
