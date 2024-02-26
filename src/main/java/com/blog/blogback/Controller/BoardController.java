package com.blog.blogback.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blog.blogback.Dto.BoardRequestDto;
import com.blog.blogback.Dto.BoardWithTagDto;
import com.blog.blogback.Dto.TagRequestDto;
import com.blog.blogback.Entity.Board;
import com.blog.blogback.Entity.Tag;
import com.blog.blogback.Repository.BoardRepository;
import com.blog.blogback.Repository.TagRepository;
import com.blog.blogback.service.BoardService;


import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.PutMapping;

@Slf4j
@RequestMapping("/api/user/board")
@RestController
public class BoardController {

    @Autowired private BoardRepository boardRepository;
    @Autowired private BoardService boardService;

    @GetMapping("/getBoards")
    public ResponseEntity<List<BoardWithTagDto>> getBoards(){
        log.info("getBoards() 진입");
        List<Board> boards = boardRepository.findAllWithTags(Sort.by(Sort.Direction.DESC, "regDate"));
        List<BoardWithTagDto> boardsWithTags = new ArrayList<>();
        for (Board board : boards) {
            List<Tag> tags = board.getTags();
            BoardWithTagDto boardWithTagDto = new BoardWithTagDto();
            boardWithTagDto.setBoard(board);
            boardWithTagDto.setTags(tags);
            boardsWithTags.add(boardWithTagDto);
        }
        //log.info("boards : {}", boards);
        return ResponseEntity.ok(boardsWithTags);
    }

    @GetMapping("/getBoardsWithTag/{tagName}")
    public List<BoardWithTagDto> getBoardsWithTag(@PathVariable String tagName){
        log.info("getBoardsWithTag() 진입");
        List<Board> boards = boardRepository.findBoardWithTags(tagName,Sort.by(Sort.Direction.DESC, "regDate"));
        List<BoardWithTagDto> boardsWithTags = new ArrayList<>();
        for (Board board : boards) {
            //List<Tag> tags = board.getTags();
            BoardWithTagDto boardWithTagDto = new BoardWithTagDto();
            boardWithTagDto.setBoard(board);
            //boardWithTagDto.setTags(tags);
            boardWithTagDto.setTagName(tagName);
            boardsWithTags.add(boardWithTagDto);
        }
        return boardsWithTags;
    }

    @GetMapping("/getBoard/{boardNo}")
    public ResponseEntity<?> getBoard(@PathVariable Long boardNo) {
        try {
            Optional<Board> boardOptional = boardRepository.findByBoardNo(boardNo);
            if(boardOptional.isPresent()){ 
                Board board = boardOptional.get();
                List<Tag> tags = board.getTags();
                // 필요한 경우 Tag 엔터티를 DTO로 변환하여 반환할 수도 있음
                // 여기서는 간단히 Tag 엔터티의 이름만 추출하여 리스트로 반환
                List<String> tagNames = tags.stream()
                            .map(Tag::getTagName)
                            .collect(Collectors.toList());
                // Board 정보와 함께 Tag 정보도 함께 반환
                Map<String, Object> response = new HashMap<>();
                response.put("board", board);
                response.put("tags", tags);
                //log.info("board : {}", board);
                //log.info("tagNameList : {}", tagNames);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("There is no data", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/saveBoard")
    public Long saveBoard(@RequestBody BoardRequestDto boardRequestDto) {
        log.info("controller.saveBoard() 진입");
        Long savedBoardNo = boardService.saveBoard(boardRequestDto);
        return savedBoardNo;
    }

    @PutMapping("/saveBoard")
    public Board update(@RequestBody BoardRequestDto boardDto) {
        log.info("boardDto : {}" + boardDto.getBoardContent());
        log.info("boardDto : {}" + boardDto.getBoardTitle());
        log.info("boardDto : {}" + boardDto.getTags());
        return boardService.update(boardDto);
    }

    @DeleteMapping("/delete/{boardNo}")
    public void delete(@PathVariable Long boardNo) {
        boardService.delete(boardNo);
    }


    @GetMapping("/getPreBoard/{boardNo}")
    public ResponseEntity<?> getPreBoard(@PathVariable Long boardNo) {
        try {
            log.info("boardNo : {}", boardNo );
            Map<String, Object> preBoard = boardService.findPreBoard(boardNo);
            Iterator<String> mapIter = preBoard.keySet().iterator();
 
            while(mapIter.hasNext()){
    
                String key = mapIter.next();
                Object value = preBoard.get( key );
    
                System.out.println(key+" : "+value);
    
            }

            if (preBoard != null) {
                return ResponseEntity.ok(preBoard);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving previous board: " + e.getMessage());
        }
    }

    @GetMapping("/getNextBoard/{boardNo}")
    public ResponseEntity<?> getNextBoard(@PathVariable Long boardNo) {
        try {
            log.info("boardNo : {}", boardNo );
            Map<String, Object> nextBoard = boardService.findNextBoard(boardNo);
            Iterator<String> mapIter = nextBoard.keySet().iterator();
 
            while(mapIter.hasNext()){
    
                String key = mapIter.next();
                Object value = nextBoard.get( key );
    
                System.out.println(key+" : "+value);
    
            }

            log.info("nextBoard : {}", nextBoard );
            if (nextBoard != null) {
                return ResponseEntity.ok(nextBoard);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving next board: " + e.getMessage());
        }
    }

    @GetMapping("/maxBoardNo")
    public ResponseEntity<?> getMaxBoardNo() {
        try {
            Long maxBoardNo = boardService.findMaxBoardNo();
            if (maxBoardNo != null) {
                return ResponseEntity.ok(maxBoardNo);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving max boardNo: " + e.getMessage());
        }
    }

    @GetMapping("/minBoardNo")
    public ResponseEntity<?> getMinBoardNo() {
        try {
            Long minBoardNo = boardService.findMinBoardNo();
            if (minBoardNo != null) {
                return ResponseEntity.ok(minBoardNo);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving max boardNo: " + e.getMessage());
        }
    }

}
