package com.server.vuebook.controller;


import com.server.vuebook.domain.Member;
import com.server.vuebook.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/member/*")
@CrossOrigin(origins = {"*"}, maxAge = 6000)
public class MemberController {

    @Autowired
    private MemberService service;

    @PostMapping("login")
    public ResponseEntity member(@RequestBody Member member){
        log.info("1."+member.getMemId());
        log.info("1."+member.getMemPwd());
        return ResponseEntity.ok().build();
    }

    @PostMapping("signUp")
    public ResponseEntity signUp(@RequestBody Member member){
        // 가져온 아이디로 데이터베이스에 다녀온 다음 값이 있다면 true로 없다면 false
        return ResponseEntity.ok().build();
    }

    @PostMapping("idCheck/{id}")
    public ResponseEntity idCheck(@PathVariable String id){
        String checkId = service.idCheck(id);
        Boolean checkBoo = false;
        if(checkId == null){
            checkBoo = true;
        }
        return ResponseEntity.ok().body(checkBoo);
    }

}
