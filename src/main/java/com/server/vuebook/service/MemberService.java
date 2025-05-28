package com.server.vuebook.service;

import com.server.vuebook.domain.Member;
import com.server.vuebook.jwt.JwtUtil;
import com.server.vuebook.repo.MemberDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
@Slf4j
@Service
public class MemberService {

    @Autowired
    private MemberDAO dao;



    private BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();

    //로그인
    public Member login(Member vo){
        Member member = dao.idCheck(vo.getMemId());
        if(member != null && bcpe.matches(vo.getMemPwd(), member.getMemPwd())){
            return member;
        }
        return null;
    }
    // 회원가입
    public void signUp(Member member){
        member.setMemPwd(bcpe.encode(member.getMemPwd()));
        dao.save(member);
    }
    // 아이디체크

    public Boolean idCheck (String id){
        Member checkId = dao.idCheck(id);
        Boolean checkBoo = false;
        if(checkId == null){
            checkBoo = true;
        }
        return checkBoo;
    }

}
