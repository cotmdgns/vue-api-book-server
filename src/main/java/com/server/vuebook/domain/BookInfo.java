package com.server.vuebook.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DynamicInsert
public class BookInfo {

    @Id
    @Column(name = "book_no")
    private int bookNo;

    @Column(name = "mem_no")
    private int memNo;

    @Column(name = "book_title")
    private String bookTitle;

    @Column(name = "book_author")
    private String bookAuthor;

    @Column(name = "book_image")
    private String bookImage;

    @Column(name = "book_link")
    private String bookLink;

    @Column(name = "book_isbn")
    private String bookIsbn;
}
