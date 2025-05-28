package com.server.vuebook.controller;


import com.server.vuebook.domain.Member;
import com.server.vuebook.jwt.JwtUtil;
import com.server.vuebook.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/api/member/*")
@CrossOrigin(origins = {"*"}, maxAge = 6000)
public class MemberController {

    @Autowired
    private MemberService service;

    @Autowired
    private JwtUtil jwtUtil;

    // 로그인
    @PostMapping("login")
    public ResponseEntity<String> member(@RequestBody Member vo){
        Member member = service.login(vo);
        if(member != null){
            String token = jwtUtil.createToken(member);
            return ResponseEntity.ok().body(token);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("아이디와 비밀번호가 틀렸습니다.");
    }

    // 회원가입
    @PostMapping("signUp")
    public ResponseEntity<String> signUp(@RequestBody Member member){
        service.signUp(member);
        return ResponseEntity.ok().build();
    }

    // 아이디체크
    @PostMapping("idCheck/{id}")
    public ResponseEntity<Boolean> idCheck(@PathVariable String id){
        return ResponseEntity.ok().body(service.idCheck(id));
    }

    // 유저의 대한 저장 공간
    @GetMapping("userBookSearchAPI/{data}")
    public ResponseEntity<String> userBookSearchAPI(@PathVariable String data, @RequestHeader("Authorization") String authorization){
        return null;
    }

}
