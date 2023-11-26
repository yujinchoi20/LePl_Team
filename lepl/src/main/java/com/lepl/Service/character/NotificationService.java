package com.lepl.Service.character;

import com.lepl.Repository.character.NotificationRepository;
import com.lepl.domain.character.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // 읽기모드
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    /**
     * join, findOne, findAll, remove, findAllWithCharacter
     */
    @Transactional // 쓰기모드
    public Long join(Notification notification) {
        notificationRepository.save(notification);
        return notification.getId();
    }

    public Notification findOne(Long notificationId) {
        return notificationRepository.findOne(notificationId);
    }

    public List<Notification> findAll() {
        return notificationRepository.findAll();
    }

    @Transactional
    public void remove(Notification notification) {
        notificationRepository.remove(notification);
    }

    public List<Notification> findAllWithCharacter(Long characterId) {
        return notificationRepository.findAllWithCharacter(characterId);
    }
}