package com.lepl.domain.character;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {
    @Id
    @GeneratedValue
    @Column(name = "notification_id")
    private Long id;

    private String content;
    private LocalDateTime startTime;
    private LocalDateTime endTime; // read 시간 -> 추후 구현

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id") // FK
    private Character character;

    /**
     * 생성 편의 메서드
     */
    public static Notification createNotification(Character character, String content) {
        Notification notification = new Notification();
        notification.character = character;
        notification.content = content;
        notification.startTime = LocalDateTime.now();
        return notification;
    }

    /**
     * 연관관계 편의 메서드
     */
    public void setCharacter(Character character) {
        this.character = character;
    }
}