package com.server.vuebook.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.vuebook.domain.Book;
import com.server.vuebook.domain.BookDTO;
import com.server.vuebook.domain.BookInfo;
import com.server.vuebook.domain.Member;
import com.server.vuebook.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("api/book/")
@CrossOrigin(origins = {"*"},allowedHeaders = {"Authorization", "Content-Type", "Accept"}, maxAge = 6000)
public class BookController {

    public static final String url = "https://openapi.naver.com/v1/search/book?";
    public static final String detailUrl = "https://openapi.naver.com/v1/search/book_adv.json?";
    private static final String CLIENT_ID = "hRpkn6R7JxUvvR2ZuCvO";
    private static final String CLIENT_SECRET = "q34yGSr7q_";

    @Autowired
    private BookService bookService;


    // 유저가 가진 정보
    @PostMapping("bookUserInfo")
    public ResponseEntity<List<BookInfo>> bookUserInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = (Member) authentication.getPrincipal(); // 올바른 타입 캐스팅
        List<BookInfo> bookInfos = bookService.userBookCheck(member.getMemNo());
        return ResponseEntity.ok(bookInfos);
    }

    // 책 체크하기
    @GetMapping("serverDetailBookCheckAPI/{data}")
    public ResponseEntity<Boolean> serverDetailBookCheckAPI(@PathVariable String data){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = (Member) authentication.getPrincipal(); // 올바른 타입 캐스팅
        int memNo = member.getMemNo(); // 또는 member.getMemName(), 등등
//        log.info("토큰 정보 : " + memNo);
//        log.info("클라이언트에서 보낸 정보 : " + data);

        // 유저 정보로 저장된 책 리스트 가져오기
        List<BookInfo> bookInfos =  bookService.userBookCheck(memNo);
        for(BookInfo book : bookInfos){
//            log.info("" + book);
            if(book.getBookIsbn().equals(data)){
                return ResponseEntity.ok(false);
            }
        }
        return ResponseEntity.ok(true);
    }
    
    // 목록 추가하기
    @PostMapping("createBookInfoAPI")
    public ResponseEntity createBookInfoAPI(@RequestBody BookInfo bookInfo){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = (Member) authentication.getPrincipal(); // 올바른 타입 캐스팅
        int memNo = member.getMemNo(); // 또는 member.getMemName(), 등등
        bookInfo.setMemNo(memNo);
        bookService.createBookUser(bookInfo);
        return null;
    };

    // 목록 제거하기
    @PostMapping("removeBookInfoAPI")
    public ResponseEntity removeBookInfoAPI(@RequestBody String isbn){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = (Member) authentication.getPrincipal(); // 올바른 타입 캐스팅
        int memNo = member.getMemNo(); // 또는 member.getMemName(), 등등
        isbn = isbn.replace("=", ""); // 뒤에 = 지워버리기
        log.info("isbn : " + isbn); // 해당 페이지에서 가져온 책 ex) 정보 97784845
        log.info("memNo : " + memNo); // 유저 정보 ex) 1
        BookDTO book = BookDTO.builder()
                .isbn(isbn)
                .memNo(memNo)
                .build();
        bookService.removeBookUser(book);
        return null;
    };


    // 디테일 ( 네이버에서 제공 ) ( 하나의 정보만 필요할 경우 )
    // 사용자의 클릭 시 (이거는 디테일 페이지로 넘어가게 만들꺼 )
    @GetMapping("serverDetailBookPage/{code}")
    public ResponseEntity serverDetailBookPageAPI(@PathVariable String code){
        String apiURL = "";
        apiURL = detailUrl + "d_isbn=" + code;

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", CLIENT_ID);
        requestHeaders.put("X-Naver-Client-Secret", CLIENT_SECRET);
        String responseBody = get(apiURL,requestHeaders);

        try{
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> jsonMap = objectMapper.readValue(responseBody,Map.class);
            return ResponseEntity.ok(jsonMap);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("json 파싱 에러");
        }
    }


    // 추천작가책
    @GetMapping("recommendationAuthorBook/{title}")
    public ResponseEntity recommendationAuthorBook(@PathVariable String title){
        BookDTO bookDTO = BookDTO.builder()
                .query(title)
                .display("7")
                .sort("sim")
                .start("1")
                .build();
        return bookAPI(bookDTO);
    }
    // 사용자의 검색
    // 스타트부분만 다시 받아서 가져오면 끝
    @GetMapping("userBookSearchAPI/")
    public ResponseEntity userBookSearchAPI(@RequestParam("title") String title, @RequestParam("number") Integer number){
        BookDTO bookDTO = BookDTO.builder()
                .query(title)
                .display("10")
                .sort("sim")
                .start(String.valueOf(number)) // 이게 위치
                .build();
        return bookAPI(bookDTO);
    }

    // 도서 검색 시스템 ( 네이버에서 제공 ) ( 여러 개 검색할 떄 )
    public ResponseEntity bookAPI(BookDTO bookDTO){
        // 기본 검색했을때 값
        String query = bookDTO.getQuery();
        String display = "100";
        String sort = "date";
        if(!bookDTO.getDisplay().isEmpty()){
            display = bookDTO.getDisplay();
        }
        if(!bookDTO.getSort().isEmpty()){
            sort = bookDTO.getSort();
        }

        try {
            query = URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("검색어 인코딩 실패",e);
        }

        String apiURL = "";

        apiURL = url + "query=" + query +
                "&display="+ display +
                "&sort=" + sort +
                "&start=" + bookDTO.getStart();
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", CLIENT_ID);
        requestHeaders.put("X-Naver-Client-Secret", CLIENT_SECRET);
        String responseBody = get(apiURL,requestHeaders);
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> jsonMap = objectMapper.readValue(responseBody,Map.class);
            return ResponseEntity.ok(jsonMap);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("json 파싱 에러");
        }
    }
    //( 네이버에서 제공하는 것들 )
    private static String get(String apiUrl, Map<String, String> requestHeaders){
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }


            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 오류 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }
    private static HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }
    private static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);


        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();


            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }


            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는 데 실패했습니다.", e);
        }
    }







}
