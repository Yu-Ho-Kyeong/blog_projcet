package com.blog.blogback.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.blog.blogback.Dto.Comment.CommentResponseDto;
import com.blog.blogback.Dto.Comment.CommentSaveDto;
import com.blog.blogback.Dto.Comment.CommentUpdateDto;
import com.blog.blogback.Entity.Board;
import com.blog.blogback.Entity.Comment;
import com.blog.blogback.Repository.CommentRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor    //final or NonNull 옵션 필드를 전부 포함한 생성자 만들어줌
@Service
public class CommentService {

    //@Autowired private CommentRepository commentRepository;
    private final CommentRepository commentRepository;

    // 댓글 count
    public int getCommentCountByBoardNo(Long boardNo) {
        log.info("getCommentCountByBoardNo()");
        log.info("boardNo : {}" , boardNo);
        log.info("count : {}" , commentRepository.countByBoardNo(boardNo));
        return commentRepository.countByBoardNo(boardNo);
    }

    // 댓글번호로 댓글정보 조회
    public CommentResponseDto findByCommentNo(Long commentNo) {
        Comment comment = commentRepository.findByCommentNo(commentNo)
                    .orElseThrow(()->new IllegalArgumentException("해당 댓글이 없습니다. commentNo="+commentNo));

        return new CommentResponseDto(comment);
    }

    // 게시글번호로 댓글정보 조회
    public List<CommentResponseDto> findByBoardNo(Long boardNo) {
        
        //List<Comment> comments = commentRepository.findByBoardNo(boardNo);
        //List<CommentResponseDto> CommentResponseDtos = new ArrayList<>();

        // for( Comment comment : comments){
        //     CommentResponseDto commentResponseDto = new CommentResponseDto(comment);
        //     CommentResponseDtos.add(commentResponseDto);          
        // }


        
        // if(CommentResponseDtos.isEmpty()){
        //     throw new IllegalArgumentException("해당 게시글에 댓글이 없습니다. boardNo=" + boardNo);
        // }

        //return CommentResponseDtos;
        return commentRepository.findByBoardNo(boardNo).stream()
            .map(CommentResponseDto :: new)
            .collect(Collectors.toList());
    }

    // 댓글 작성
    @Transactional  //db 트랜잭션 자동으로 commit 해줌
    public Long addComment(CommentSaveDto saveDto) {
        return commentRepository.save(saveDto.toEntity()).getCommentNo();
    }

    // 댓글 수정
    @Transactional  
    public Long modifyComment(Long commentNo, CommentUpdateDto updateDto){
        Comment comment = commentRepository.findByCommentNo(commentNo)
                .orElseThrow(() -> new RuntimeException("해당 댓글을 찾을 수 없습니다. id=" + commentNo));

        //JPA의 영속성 컨텍스트 덕분에 entity 객체의 값만 변경하면 자동으로 변경사항 반영함!
        //따라서 repository.update 를 쓰지 않아도 됨.
        comment.update(updateDto.getCommentContent());
        return updateDto.getCommentNo();
    }

    @Transactional
    public void deleteComment(Long commentNo){
        commentRepository.deleteById(commentNo);
    }
}
