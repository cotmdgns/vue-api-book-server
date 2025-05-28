package com.server.vuebook.repo;

import com.server.vuebook.domain.BookInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookInfoDAO extends JpaRepository<BookInfo, Integer> {

    //유저 정보로 저정된 유저 정보 가져오기
    @Query(value = "SELECT * FROM book_info where mem_no = :num",nativeQuery = true)
    List<BookInfo> Info(@Param("num")int num);

    // 삭제하기
    @Modifying
    @Query(value = "DELETE FROM book_info WHERE mem_no = :memNo  AND book_isbn = :isbn", nativeQuery = true)
    void bookDelete(@Param("memNo") int memNo, @Param("isbn") String isbn);

}
