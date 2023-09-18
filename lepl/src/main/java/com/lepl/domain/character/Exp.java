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

    private Long expAll;
    private int level;
}
