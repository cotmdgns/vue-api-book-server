package com.server.vuebook.repo;

import com.server.vuebook.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberDAO extends JpaRepository<Member,Integer> {

    // 아이디체크
    @Query(value = "SELECT * FROM member WHERE mem_id = :id",nativeQuery = true)
    Member idCheck(@Param("id")String id);

}
