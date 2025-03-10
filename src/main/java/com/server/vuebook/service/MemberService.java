package com.server.vuebook.service;

import com.server.vuebook.domain.Member;
import com.server.vuebook.repo.MemberDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    @Autowired
    private MemberDAO dao;

    public void signUp(Member member){
        dao.save(member);
    }

    public String idCheck (String id){
        return dao.idCheck(id);
    }
}
