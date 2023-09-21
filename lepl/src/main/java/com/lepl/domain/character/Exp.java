package com.lepl.domain.character;


import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
@Getter @Setter
public class Exp {
    @Id @GeneratedValue
    @Column(name = "exp_id")
    private Long id;

    private Long expAll; //누적 경험치
    private Long expPre; //현재 경험치 -> 상점에서 사용하고 남은 경험치
    private Long level; //누적 경험치를 가지고 공식에 따라 표현한 레벨
}
