package com.lepl.domain.character;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Follow {

    @Id @GeneratedValue
    @Column(name = "follow_id")
    private Long id;

    private Long followerId; // from
    private Long followingId; // to

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id") // FK
    private Character character;

    /*
     * 생성 편의 메서드
     */
    public static Follow createFollow(Character character, Long followingId) {
        Follow follow = new Follow();
        follow.character = character;
        follow.followingId = followingId;
        return follow;
    }

    /*
     * setter
     */
    public void setCharacter(Character character) { //연관관계 편의 메서드에 사용됨
        this.character = character;
    }
    public void setFollowerId(Long followerId) {
        this.followerId = followerId;
    }
}