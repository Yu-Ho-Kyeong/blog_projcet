package com.blog.blogback.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import com.blog.blogback.Dto.BoardRequestDto;
import com.blog.blogback.Dto.Board.BoardImgRequestDto;
import com.blog.blogback.Dto.Board.BoardImgSaveDto;
import com.blog.blogback.Entity.Board;
import com.blog.blogback.Entity.BoardImg;
import com.blog.blogback.Entity.Tag;
import com.blog.blogback.Entity.User;
import com.blog.blogback.Repository.BoardImgRepository;
import com.blog.blogback.Repository.BoardRepository;
import com.blog.blogback.Repository.TagRepository;
import com.blog.blogback.Repository.UserRepository;
import com.blog.blogback.common.Exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.time.format.DateTimeFormatter;


@Slf4j
@RequiredArgsConstructor    //final or NonNull 옵션 필드를 전부 포함한 생성자 만들어줌
@Service
public class BoardService {
   
    @Value("${upload.board.path}")      // 게시글 저장시 업로드 경로
    private String uploadDir;
    private final BoardRepository boardRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final BoardImgRepository boardImgRepository;

    // 게시글 저장
    @Transactional
    public Long saveBoard(BoardRequestDto boardRequestDto) {
        User user = userRepository.findById(boardRequestDto.getUserNo())
            .orElseThrow(() -> new NotFoundException("Could not found userNo " + boardRequestDto.getUserNo()));

        Board board = boardRequestDto.toEntity(user);
        return boardRepository.save(board).getBoardNo();
    }

    // 게시글 수정
    @Transactional
    public Board update(BoardRequestDto boardRequestDto) {
        List<Tag> tags = boardRequestDto.getTags();
        Board board = boardRepository.findById(boardRequestDto.getBoardNo())
                .orElseThrow(() -> new NotFoundException("해당 게시글을 찾을 수 없습니다. id=" + boardRequestDto.getBoardNo()));

        board.update(boardRequestDto.getBoardTitle(), boardRequestDto.getBoardContent());
    
        // 태그 엔티티를 갱신
        board.getTags().clear(); // 기존 태그 삭제
        if (tags != null) {
            for (Tag tag : tags) {
                board.addTag(tag); // 새로운 태그 추가
            }
        }
        return boardRepository.save(board);
    }
    // 게시글 이미지 업로드
    @Transactional
    public List<String> uploadImg(@RequestBody BoardImgRequestDto boardImgRequestDto){
        // 파일 경로 설정
        Path uploadPath = Paths.get(uploadDir);
        if(!Files.exists(uploadPath)){
            try{
                Files.createDirectories(uploadPath);
            }catch(IOException e){
                throw new NotFoundException("디렉토리 생성 실패");
            }
        }
        List<String> fakePaths = new ArrayList<>();
        // 파일명 중복 방지를 위한 날짜시간 생성
        for(MultipartFile file : boardImgRequestDto.getFiles()){
            log.info("boardImgRequestDto.getFiles : " + file);
            LocalDateTime now = LocalDateTime.now();
            String fmtNow = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            
            String fileName = fmtNow + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);

            try{
                Files.copy(file.getInputStream(), filePath);
              
                String[] pathPart = filePath.toString().split("public");
                String fakePath = pathPart[1];
                fakePaths.add(fakePath);
                
            }catch(IOException e){
                throw new NotFoundException("파일 업로드 실패");
            }
        }
	
        return fakePaths;
    }

    // 게시글 content 및 이미지 저장 
    @Transactional
    public Map<String, Object> updateContentAndAdImg(BoardImgSaveDto boardImgSaveDto) {
        Map<String, Object> params = new HashMap<>();    
        Board board = boardRepository.findByBoardNo(boardImgSaveDto.getBoardNo())
            .orElseThrow(() -> new NotFoundException("해당 게시글을 찾을 수 없습니다." + boardImgSaveDto.getBoardNo()));

        // 게시글 내용 업데이트
        board.updateContent(boardImgSaveDto.getBoardContent());
       
        // 이미지 경로들을 저장하고 이미지를 게시글에 연결하여 저장
        for (String imgPath : boardImgSaveDto.getImgPathList()) {
            BoardImg boardImg = BoardImg.builder()
                .board(board)
                .imgPath(imgPath)
                .build();
            boardImgRepository.save(boardImg);
        }
        boardRepository.save(board);
        params.put("board", board);
        return params;
    }
    // 게시글 삭제
    @Transactional
    public void delete(Long boardNo) {
        Board entity = boardRepository.findById(boardNo)
            .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다." + boardNo));
        boardRepository.delete(entity);
    }

    // 태그 정보 조회
    public List<Object[]> getTag(){
        List<Object[]> tagInfo = tagRepository.boardCountByTagName();
        return tagInfo;
    }
    // 태그및 게시글 조회
    public List<Board> findBoardWithTags(String tagName) {
        Sort sort = Sort.by(Sort.Direction.DESC, "regDate");
        return boardRepository.findBoardWithTags(tagName, sort);
    }
    // 태그 count
    public int allTagCnt(){
        int cnt = tagRepository.allTagCnt();
        return cnt;
    }
    // 이전 게시글 정보 조회
    public Map<String, Object> findPreBoard(Long boardNo) {
        return boardRepository.findPreByBoardNo(boardNo);
    }
    // 다음 게시글 정보 조회
    public Map<String, Object> findNextBoard(Long boardNo) {
        return boardRepository.findNextByBoardNo(boardNo);
    }
    // 게시글번호 max값 찾기
    public Long findMaxBoardNo() {
        return boardRepository.findMaxBoardNo();
    }
    // 게시글 번호 min값 찾기
    public Long findMinBoardNo() {
        return boardRepository.findMinBoardNo();
    }
}