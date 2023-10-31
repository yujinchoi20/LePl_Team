package com.lepl.domain.character;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String type;
    private String name;

    private int price;
    private int purchase_quantity;

    private LocalDateTime start_time;
    private LocalDateTime end_time;

    /*
    * 필요시 생성 편의 메서드, 비즈니스 편의 메서드 생성
    * */
}
