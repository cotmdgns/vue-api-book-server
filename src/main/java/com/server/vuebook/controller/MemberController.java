package com.server.vuebook.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/member/*")
@CrossOrigin(origins = {"*"}, maxAge = 6000)
public class MemberController {

    @GetMapping("")
    public ResponseEntity Member(int i){
        log.info("옛기억들 하나씩 돌아온다.",i);
        return ResponseEntity.ok().build();
    }

}
