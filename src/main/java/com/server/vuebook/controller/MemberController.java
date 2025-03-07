package com.server.vuebook.controller;


import com.server.vuebook.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/member/*")
@CrossOrigin(origins = {"*"}, maxAge = 6000)
public class MemberController {

    @PostMapping("login")
    public ResponseEntity Member(@RequestBody Member member){
        log.info("옛기억들 하나씩 돌아온다."+member.getMemId());
        log.info("옛기억들 하나씩 돌아온다. ID: {}", member.getMemId());
        log.info("옛기억들 하나씩 돌아온다. PWD: {}", member);
        return ResponseEntity.ok().build();
    }

}
