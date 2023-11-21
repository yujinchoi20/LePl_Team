package com.lepl.domain.character;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class  Item {

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
    * 생성 편의 메서드
    * */
    public static Item createItem(String type, String name, int price, int purchase_quantity, LocalDateTime start_time, LocalDateTime end_time) {
        Item item = new Item();

        item.setType(type);
        item.setName(name);
        item.setPrice(price);
        item.setPurchase_quantity(purchase_quantity);
        item.setStart_time(start_time);
        item.setEnd_time(end_time);

        return item;
    }
}
