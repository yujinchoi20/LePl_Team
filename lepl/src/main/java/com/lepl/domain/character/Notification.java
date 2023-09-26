package com.lepl.domain.character;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Notification {

    @Id @GeneratedValue
    @Column(name = "notification_id")
    private Long id;

    private String content;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id")
    private Character character;

    /*
    * 생성 편의 메서드
    */
    public static Notification createNotification(Character character, String content) {
        Notification notification = new Notification();

        notification.character = character;
        notification.content = content;
        notification.startTime = LocalDateTime.now();

        return notification;
    }
}
