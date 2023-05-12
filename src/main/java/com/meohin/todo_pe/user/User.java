package com.meohin.todo_pe.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity // Hibernate에게 이 클래스에서 테이블을 만들도록 지시
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String snsType;

    @Column(unique = true)
    private String snsID;

    private LocalDateTime authorizeDate;
}