package com.server.vuebook.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@DynamicInsert

public class Member {

    @Id
    @Column(name = "mem_no")
    private int memNo;

    @Column(name = "mem_id")
    private String memId;

    @Column(name = "mem_pwd")
    private String memPwd;

    @Column(name = "mem_name")
    private String memName;

    @Column(name = "email")
    private String email;

}
