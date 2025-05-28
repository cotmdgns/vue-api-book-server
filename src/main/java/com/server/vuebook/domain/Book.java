package com.server.vuebook.domain;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamicInsert
public class Book {
    //                                                  DB에 들어갈 얘들
    private String title;                               // 책 이름
    private String link;                                // 링크 주소
    private String image;                               // 이미지
    private String author;                              // 저자
    private String discount; // 가격
    private String publisher; // 출판사
    private String isbn; // 번호
    private String description;// 내용
    private String pubdate; // 출시일

}
