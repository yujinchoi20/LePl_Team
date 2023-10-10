package com.lepl.domain.member;

import com.lepl.domain.task.Lists;
import com.lepl.domain.character.Character;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
@Slf4j
public class Member {
    @Id @GeneratedValue
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

    //==생성 편의 메서드==//
    /**코드 중복 감소 효과 및 uid가 not null임을 강조 및 nickname 처리 등등 하기 위해*/
    public static Member createMember(String uid, String nickname, List<Lists> lists, Profile profile, Character character) {
        Member member = new Member();

        if(uid == null) {
            log.info("uid", "uid 가 필요합니다.");
        }
        if(nickname == null) {
            nickname = "닉네임을 등록해주세요";
        }

        member.setUid(uid);
        member.setNickname(nickname);
        member.setLists(lists);
        member.setProfile(profile);
        member.setCharacter(character);
        return member;
    }
    public static Member createMember(String uid, String nickname) {
        Member member = new Member();

        if(uid == null) {
            log.info("uid", "uid 가 필요합니다.");
        }
        if(nickname == null) {
            nickname = "닉네임을 등록해주세요";
        }

        member.setUid(uid);
        member.setNickname(nickname);
        return member;
    }
}
