package com.lepl.api.character;


import com.lepl.Service.character.CharacterService;
import com.lepl.Service.character.FollowService;
import com.lepl.Service.character.NotificationService;
import com.lepl.Service.member.MemberService;
import com.lepl.api.argumentresolver.Login;
import com.lepl.domain.character.Character;
import com.lepl.domain.character.Follow;
import com.lepl.domain.character.Notification;
import com.lepl.domain.member.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.lepl.util.Messages.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/follow")
public class FollowApiController {
    private final FollowService followService;
    private final MemberService memberService;
    private final CharacterService characterService;
    private final NotificationService notificationService;

    /**
     * create, delete, findFollowing, findFollower
     */

    /**
     * 팔로우 생성 API -> 알림 포함!
     * 입력 -> JSON
     * followingId를 입력 받는다.
     */
    @PostMapping("/new")
    public ResponseEntity<String> create(@Login Long memberId, @RequestBody Map<String, Long> request) {
        // 팔로우 추가!
        Long followingId = request.get("followingId");
        Member member = memberService.findOne(memberId);
        Character character = characterService.findCharacterWithMember(memberId);
        Follow follow = Follow.createFollow(character, followingId);
        followService.join(follow); // 중복검증 포함

        // 알림 추가! -> 상대방 캐릭터에 알림 추가목적
        log.debug("followingId : {}", followingId);
        Character findCharacter = characterService.findOne(followingId); // 상대방 캐릭터
        if (findCharacter == null) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(FAIL_FOLLOW);
        }
        Notification notification = Notification.createNotification(findCharacter, "테스트 알림 : " + member.getNickname() + "가 팔로우 하였습니다.");
        notificationService.join(notification);
        return ResponseEntity.status(HttpStatus.CREATED).body(SUCCESS_FOLLOW);
    }

    /**
     * 팔로우 제거 API
     * 입력 -> JSON
     * followId를 입력 받는다.
     * 참고) 꼭 자신의 캐릭터 ID 를 FK 로 가지는 Follow 테이블만 삭제 가능하게 검증 필수
     */
    @PostMapping("/delete")
    public ResponseEntity<String> delete(@Login Long memberId, @RequestBody Map<String, Long> request) {
        Long followId = request.get("followId");
        Character character = characterService.findCharacterWithMember(memberId);
        Follow follow = followService.findOne(followId);
        if (follow.getCharacter().getId().longValue() == character.getId().longValue()) {
            followService.remove(follow);
            return ResponseEntity.status(HttpStatus.OK).body(SUCCESS_FOLLOW_CANCEL);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(UNAUTHORIZED_FOLLOW_CANCEL);
        }
    }

    /**
     * 팔로잉 조회 API
     * 자신이 팔로우 한 사람들을 조회
     */
    @GetMapping("/ing/all")
    public ResponseEntity<List<ResFollowingDto>> findFollowing(@Login Long memberId) {
        Character character = characterService.findCharacterWithMember(memberId);
        List<Follow> follows = followService.findAllWithFollowing(character.getId());
        if (follows.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        List<ResFollowingDto> result = follows.stream()
                .map(o -> new ResFollowingDto(o))
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * 팔로워 조회 API
     * 자신을 팔로우 한 사람들을 조회
     */
    @GetMapping("/er/all")
    public ResponseEntity<List<ResFollowerDto>> findFollower(@Login Long memberId) {
        Character character = characterService.findCharacterWithMember(memberId);
        List<Follow> follows = followService.findAllWithFollower(character.getId());
        if (follows.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        List<ResFollowerDto> result = follows.stream()
                .map(o -> new ResFollowerDto(o))
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


    // DTO
    @Getter
    static class ResFollowingDto {
        private Long followId;
        private Long followerId;
        private Long followingId;

        public ResFollowingDto(Follow follow) {
            followId = follow.getId();
            followerId = follow.getFollowerId(); // from
            followingId = follow.getFollowingId(); // to
        }
    }

    @Getter
    static class ResFollowerDto {
        private Long followId;
        private Long followerId;
        private Long followingId;

        public ResFollowerDto(Follow follow) {
            followId = follow.getId();
            followerId = follow.getFollowerId(); // from
            followingId = follow.getFollowingId(); // to
        }
    }
}