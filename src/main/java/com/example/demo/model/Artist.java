package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "artist")
public class Artist {

    @Id
    private String name;  // 아티스트 이름을 기본 키로 사용

    // 기본 생성자 (JPA에서 엔터티를 생성하기 위해 필요)
    public Artist() {}

    // 아티스트 이름을 받아서 객체를 생성하는 생성자
    public Artist(String name) {
        this.name = name;
    }

    // Getter and Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
