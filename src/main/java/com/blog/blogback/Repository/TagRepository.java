package com.blog.blogback.Repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blog.blogback.Entity.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long>{
    Optional<Tag> findByTagNo(Long tagNo);   //태그번호로 태그정보 불러오기
}
