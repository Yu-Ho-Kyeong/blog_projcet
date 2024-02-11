package com.blog.blogback.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.blogback.Dto.BoardRequestDto;
import com.blog.blogback.Entity.Board;
import com.blog.blogback.Repository.BoardRepository;
import com.blog.blogback.service.BoardService;

import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.PutMapping;


@RequestMapping("/api/user/board")
@RestController
public class BoardController {

    @Autowired 
    private BoardRepository boardRepository;

    @Autowired
    private BoardService boardService;

    @GetMapping("/getBoards")
    public List<Board> getBoards(){
        List<Board> boards = boardRepository.findAll();
        return boards;
    }

    @GetMapping("/{boardNo}")
    public ResponseEntity<?> getBoard(@PathVariable Long boardNo) {
        try {
            Optional<Board> boardOptional = boardRepository.findByBoardNo(boardNo);
            if(boardOptional.isPresent()){
                Board board = boardOptional.get();
                return new ResponseEntity<>(board, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("There is no data", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/saveBoard")
    public ResponseEntity<Long> saveBoard(@RequestBody BoardRequestDto boardRequestDto) {
        try {
            // BoardService를 통해 새로운 게시글을 저장하고 생성된 boardNo를 받아옴
            Long savedBoardNo = boardService.saveBoard(boardRequestDto);
            return new ResponseEntity<>(savedBoardNo, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/saveBoard")
    public Board update(@RequestBody BoardRequestDto boardDto) {
        return boardService.update(boardDto);
    }

    @DeleteMapping("/{boardNo}")
    public void delete(@PathVariable Long boardNo) {
        boardService.delete(boardNo);
    }
}