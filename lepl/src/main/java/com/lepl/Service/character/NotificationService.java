package com.lepl.Service.character;

import com.lepl.Repository.character.NotificationRepository;
import com.lepl.domain.character.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) //읽기 모드
@RequiredArgsConstructor
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    /*
    * save, findOne, findAll, remove, findAllWithCharacter
    */

    @Transactional
    public Long save(Notification notification) {
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

    public List<Notification> findAllWriteCharacter(Long characterId) {
        return notificationRepository.findAllWithCharacter(characterId);
    }
}
