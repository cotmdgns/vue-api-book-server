package com.server.vuebook.repo;

import com.server.vuebook.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberDAO extends JpaRepository<Member,Integer> {
}
