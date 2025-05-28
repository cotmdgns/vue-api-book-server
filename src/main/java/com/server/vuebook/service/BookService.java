package com.server.vuebook.service;

import com.server.vuebook.domain.BookDTO;
import com.server.vuebook.domain.BookInfo;
import com.server.vuebook.repo.BookInfoDAO;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Slf4j
@Service
public class BookService {

    @Autowired
    private BookInfoDAO bookInfoDAO;

    // 책 추가하기
    public void createBookUser(BookInfo bookInfo) {
        log.info("컨트롤 정보 : " + bookInfo);
        bookInfoDAO.save(bookInfo);
    }

    // 책 삭제하기
    @Transactional
    public void removeBookUser(BookDTO bookDTO){
        bookInfoDAO.bookDelete(bookDTO.getMemNo(),bookDTO.getIsbn());
        log.info("삭제 끝");
    }


    // 책 저장 체크용 or 리스트 뽑기
    public List<BookInfo> userBookCheck(int memNo){
        List<BookInfo> bookInfo = bookInfoDAO.Info(memNo);
        return bookInfo;
    }



}
