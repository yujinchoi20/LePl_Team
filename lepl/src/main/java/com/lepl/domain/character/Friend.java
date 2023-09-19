package com.lepl.domain.character;

import jakarta.persistence.*;

@Entity
public class Friend {

    @Id @GeneratedValue
    @Column(name = "friend_id")
    private Long id;

    private String friendNickname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character")
    private Character character;
}
