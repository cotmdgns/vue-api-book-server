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
public class BookDTO {

    private String query;
    private String display;
    private String start;
    private String sort;

    // 디테일에 사용되는 DTO
    private String d_titl;
    private String d_isbn;


    // 삭제하기 DTO
    private int memNo;
    private String isbn;

}
