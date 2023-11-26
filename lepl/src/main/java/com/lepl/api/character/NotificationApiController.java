package com.lepl.api.character;


import com.lepl.Service.character.CharacterService;
import com.lepl.Service.character.NotificationService;
import com.lepl.api.argumentresolver.Login;
import com.lepl.domain.character.Character;
import com.lepl.domain.character.Notification;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/notification")
public class NotificationApiController {
    private final CharacterService characterService;
    private final NotificationService notificationService;

    /**
     * findAllWithCharacter
     */

    /**
     * 알림 조회 API
     */
    @GetMapping("/all")
    public ResponseEntity<List<FindNotificationDto>> findAllWithCharacter(@Login Long memberId) {
        Character character = characterService.findCharacterWithMember(memberId);
        List<Notification> notifications = notificationService.findAllWithCharacter(character.getId());
        if (notifications.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null); // 204
        }
        List<FindNotificationDto> result = notifications.stream()
                .map(o -> new FindNotificationDto(o))
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


    // DTO
    @Getter
    static class FindNotificationDto {
        private Long id;
        private String content;
        private LocalDateTime startTime;
        private LocalDateTime endTime;

        public FindNotificationDto(Notification notification) {
            this.id = notification.getId();
            this.content = notification.getContent();
            this.startTime = notification.getStartTime();
            this.endTime = notification.getEndTime();
        }
    }
}