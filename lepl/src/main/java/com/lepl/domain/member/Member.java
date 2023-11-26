package com.lepl.domain.member;

import com.lepl.domain.character.Character;
import com.lepl.domain.task.Lists;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
@Table(name = "MEMBER", indexes = @Index(name = "IDX_MEMBER_ID", columnList = "member_id desc"))
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id; // DB PK
    @Column(nullable = false) // Not Null
    private String uid; // Entity ID => 대체키

    private String nickname;

    @OneToMany(mappedBy = "member") // 양방향
    private List<Lists> lists = new ArrayList<>(); // 컬렉션은 필드에서 바로 초기화

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id") // FK
    private Profile profile;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id") // FK
    private Character character;

    /**
     * 연관관계 편의 메서드
     */
    public void addLists(Lists lists) {
        lists.setMember(this); // Lists(엔티티)에 Member(엔티티)참조
        this.lists.add(lists); // Member(엔티티)의 lists 리스트에 Lists(엔티티)추가
    }
    public void setCharacter(Character character) {
        this.character = character;
    }
    public void setId(long id) {
        this.id = id;
    }

    //==생성 편의 메서드==//
    public static Member createMember(String uid, String nickname) {
        Member member = new Member();
        if (uid == null) log.info("uid", "uid 가 필요합니다.");
        if (nickname == null) nickname = "닉네임을 등록해주세요";
        member.uid = uid;
        member.nickname = nickname;
        return member;
    }
}