package com.blog.blogback.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.blog.blogback.Dto.BoardRequestDto;
import com.blog.blogback.Dto.JwtToken;
import com.blog.blogback.Dto.UserDto;
import com.blog.blogback.Dto.UserIntroDto;
import com.blog.blogback.Dto.UserJoinDto;
import com.blog.blogback.Dto.UserLoginDto;
import com.blog.blogback.Dto.UserRequestDto;
import com.blog.blogback.Entity.Board;
import com.blog.blogback.Entity.User;
import com.blog.blogback.Repository.UserRepository;
import com.blog.blogback.common.JwtTokenProvider;
import com.blog.blogback.security.CustomUserDetails;
import com.blog.blogback.security.CustomUserDetailsService;
import com.blog.blogback.security.CustomUserDtailsRepository;
import com.blog.blogback.security.SecurityUtil;
import com.blog.blogback.service.UserService;


import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;
import org.springframework.web.bind.annotation.RequestParam;






@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDtailsRepository customUserDtailsRepository;
    private final CustomUserDetailsService customUserDetailsService;

    @GetMapping("/getUsers")
    public List<User> getMembers(){
        List<User> users = userRepository.findAll();
        return users; 
    }

    // @PostMapping("/login")
    // public ResponseEntity login( @RequestBody Map<String, String> params, HttpServletResponse res){
    //     //params를 통해 userId와 userPw 키를 가진 Map으로 전달, HttpServletResponse를 통해 서버에서 클라이언트로 응답
    //     User user = userRepository.findByUserIdAndUserPw(params.get("userId"), params.get("userPw"));
    //     if(user != null){
    //         Long userNo = user.getUserNo();
    //         String token  = jwtService.getToken("userNo", userNo);

    //         Cookie cookie = new Cookie("token", token);  
    //         //cookie를 생성하여 jwtService.getToken을 통해 생성한 토큰을 cookie에 담아준다.
    //         cookie.setHttpOnly(true);   // js 접근 불가
    //         cookie.setPath("/");            // 쿠키의 유효경로 :"/"는 모든 경로에서 사용
    //         cookie.setMaxAge(60*60*24*30);      // 30일 동안 쿠키 유지
            
    //         System.out.println("cookie : " + cookie);

    //         res.addCookie(cookie);              // 응답에 쿠키를 추가하여 클라이언트에게 토큰 전달
    //         //return ResponseEntity.ok().build(); //성공적인 응답을 전송. 200 OK 상태 코드사용
    //         return new ResponseEntity<>(userNo, HttpStatus.OK); // 응답값을 userNo로 준다.
    //     }
    //     throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    // }

    @PostMapping("/login")
    public Map<String, Object> login( @RequestBody UserLoginDto userLogintDto){
        System.out.println("testtttttttttttt1");
        String username = userLogintDto.getUserId();
        String password = userLogintDto.getUserPw();
        Optional<CustomUserDetails> loginUser = customUserDtailsRepository.findByUserId(username);

        log.info("request username = {}, password = {}" + username, password);
        System.out.println("testtttttttttttt2");

        JwtToken jwtToken = userService.login(username, password);

        Map<String, Object> param = new HashMap<>();
        if (loginUser.isPresent()) {
            param.put("user_role", loginUser.get().getAuthorities().stream().findFirst().get().getAuthority());
        }
        param.put("user_id", username);
        param.put("user_no", loginUser.get().getId());
        log.info("user_no : {}", loginUser.get().getId());
        param.put("user_access_token", jwtToken.getAccessToken());
        param.put("user_refresh_token", jwtToken.getRefreshToken());


        log.info("jwtToken accsessToken = {}, refreshToekn = {}" + jwtToken.getAccessToken(), jwtToken.getRefreshToken());
        return param;
    }

    @PostMapping("/refreshAccessToken")
    public ResponseEntity<?> refreshAccessToken(@RequestBody Map<String, String> requestData) {
        log.info("refreshAccessToken() 진입");
        String refreshToken = requestData.get("refreshToken");
        log.info("refreshAccessToken()_refreshToken : {}", refreshToken);
        // refreshToken의 유효성을 확인하고 새로운 accessToken을 발급
        if (jwtTokenProvider.validateToken(refreshToken)) {
            String username = jwtTokenProvider.extractUsername(refreshToken); 
            log.info("refreshAccessToken()_username : {}", username);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            log.info("refreshAccessToken()_userDetails : {}", userDetails);

            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            JwtToken newAccessToken = jwtTokenProvider.generateToken(authentication);
            log.info("refreshAccessToken()_newAccessToken : {}", newAccessToken);
            // 새로운 accessToken을 응답으로 반환
            Map<String, Object> response = new HashMap<>();
            response.put("accessToken", newAccessToken.getAccessToken());
            log.info("refreshAccessToken()_responseMap : {}", response);
            return ResponseEntity.ok(response);
        } else {
            // refreshToken이 유효하지 않을 경우
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refreshToken");
        }
    }


    // @GetMapping("/check")
    // public ResponseEntity check(@CookieValue(value = "token", required = false) String token) {
    //     boolean validateToken = jwtTokenProvider.validateToken(token);
    //     System.out.println("validateToken check : " + validateToken);
    //     if(validateToken){
    //         int userNo = Integer.parseInt(claims.get("userNo").toString());
    //         return new ResponseEntity<>(userNo, HttpStatus.OK);
    //     }
    //     return new ResponseEntity<>(null, HttpStatus.OK);
    // }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody UserJoinDto userJoinDto) {
        // try {
        //     userService.signup(userJoinRequest);
        //     return new ResponseEntity<>("회원가입 성공", HttpStatus.OK);
        // } catch (Exception e) {
        //     // 예외가 발생하면 실패 상태 코드와 메시지를 반환
        //     return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        // }
        UserDto savedUserDto = userService.signUp(userJoinDto);
        return ResponseEntity.ok(savedUserDto);
    }

    @PostMapping("/test")
    public String test() {
        return SecurityUtil.getCurrentUsername();
    }

    @GetMapping("/getIntro/{userNo}")
    public ResponseEntity<String> getIntro(@PathVariable Long userNo) {
        try {
            Optional<User> userOptional = userRepository.findById(userNo);
            if(userOptional.isPresent()){
                String intro = userOptional.get().getIntroduction();
                return new ResponseEntity<>(intro, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("There is no data", HttpStatus.BAD_REQUEST);
    }
    

    @PostMapping("/saveIntro")
    public ResponseEntity<String> saveIntro(@RequestBody UserIntroDto userIntroDto) {
        log.info("saveIntro()");
        try {
            log.info("받아온 data : {}", userIntroDto.getIntroduction());
            String currentUserId = getCurrentUserId();
            log.info("userId : {}", currentUserId);
            Optional<User> userOptional = userRepository.findByUserId(currentUserId);

            if(userOptional.isPresent()){
                User user = userOptional.get();
                user.setIntroduction(userIntroDto.getIntroduction());
                userRepository.save(user);
                //String intro = userOptional.get().getIntroduction();
                log.info("user : {}", user.getIntroduction());
                //Map<String, String> param = new HashMap<>();
                // param.put("userNo", user.getUserNo());
                //param.put("introduction", user.getIntroduction());

                log.info("수정된 introduction : {}", user.getIntroduction());
                return new ResponseEntity<>(user.getIntroduction(), HttpStatus.OK);
            }else{
                // 사용자를 찾지 못한 경우 예외 처리
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }

    private String getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication.getName(); // 현재 사용자의 ID를 반환
}
    
}
