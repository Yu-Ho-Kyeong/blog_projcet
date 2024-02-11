package com.blog.blogback.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.blog.blogback.Dto.UserRequestDto;
import com.blog.blogback.Entity.User;
import com.blog.blogback.Repository.UserRepository;
import com.blog.blogback.service.UserService;
import com.blog.blogback.service.jwtService;
import com.blog.blogback.service.jwtServiceImpl;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.boot.web.server.Cookie.SameSite;





@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserRepository userRepository;
    private final jwtService jwtService;
    private final UserService userService;

    @GetMapping("/getUsers")
    public List<User> getMembers(){
        List<User> users = userRepository.findAll();
        return users; 
    }

    @PostMapping("/login")
    public ResponseEntity login( @RequestBody Map<String, String> params, HttpServletResponse res){
        //params를 통해 userId와 userPw 키를 가진 Map으로 전달, HttpServletResponse를 통해 서버에서 클라이언트로 응답
        User user = userRepository.findByUserIdAndUserPw(params.get("userId"), params.get("userPw"));
        if(user != null){
            int userNo = user.getUserNo();
            String token  = jwtService.getToken("userNo", userNo);

            Cookie cookie = new Cookie("token", token);  
            //cookie를 생성하여 jwtService.getToken을 통해 생성한 토큰을 cookie에 담아준다.
            cookie.setHttpOnly(true);   // js 접근 불가
            cookie.setPath("/");            // 쿠키의 유효경로 :"/"는 모든 경로에서 사용
            cookie.setMaxAge(60*60*24*30);      // 30일 동안 쿠키 유지
            
            System.out.println("cookie : " + cookie);

            res.addCookie(cookie);              // 응답에 쿠키를 추가하여 클라이언트에게 토큰 전달
            //return ResponseEntity.ok().build(); //성공적인 응답을 전송. 200 OK 상태 코드사용
            return new ResponseEntity<>(userNo, HttpStatus.OK); // 응답값을 userNo로 준다.
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/check")
    public ResponseEntity check(@CookieValue(value = "token", required = false) String token) {
        Claims claims = jwtService.getClaims(token);
        System.out.println("claims check : " + claims);
        if(claims != null){
            int userNo = Integer.parseInt(claims.get("userNo").toString());
            return new ResponseEntity<>(userNo, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody UserRequestDto userJoinRequest) {
        try {
            userService.signup(userJoinRequest);
            return new ResponseEntity<>("회원가입 성공", HttpStatus.OK);
        } catch (Exception e) {
            // 예외가 발생하면 실패 상태 코드와 메시지를 반환
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
}
