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
     * 팔로우 생성 API -> 중복검증 꼭 해야함! 나중에 추가하겠음
     */
    @PostMapping("/new")
    public ResponseEntity<String> addFollow(@Login Long memberId, @RequestBody Map<String, Long> json) {
        Long followingId = json.get("followingId");
        Member member = memberService.findOne(memberId);
        Character character = member.getCharacter();
        Follow follow = Follow.createFollow(character, followingId);
        followService.save(follow);

        //알림 생성
        log.debug("followingId : {}", followingId);
        Character findCharacter = characterService.findOne(followingId);

        if(findCharacter == null) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 캐릭터입니다.");
        }
        Notification notification = Notification.createNotification(findCharacter, "테스트 알림 : " + member.getNickname() + "가 팔로우 하였습니다.");
        notificationService.save(notification);

        return ResponseEntity.status(HttpStatus.CREATED).body("팔로우 추가");
    }

    /**
     * 팔로우 제거 API
     * 꼭 자신의 캐릭터 ID 를 FK 로 가지는 Follow 테이블만 삭제 가능하게 검증 필수
     */
    @PostMapping("/delete")
    public ResponseEntity<String> deleteFollow(@Login Long memberId, @RequestBody Map<String, Long> json) {
        Long followId = json.get("followId");
        Character character = memberService.findOne(memberId).getCharacter();
        Follow follow = followService.findOne(followId);

        if(follow.getCharacter().getId() == character.getId()) {
            followService.remove(follow);
            return ResponseEntity.status(HttpStatus.OK).body("팔로우 제거");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("권한이 없습니다.");
        }
    }

    /**
     * 팔로잉 조회 API -> 해당 캐릭터의 팔로잉
     */
    @GetMapping("/ing/all")
    public ResponseEntity<List<ResFollowingDto>> findFollowing(@Login Long memberId) {
        Character character = memberService.findOne(memberId).getCharacter();
        List<Follow> follows = followService.findAllWithFollowing(character.getId());
        if(follows.isEmpty()) return null;
        List<ResFollowingDto> result = follows.stream()
                .map(o -> new ResFollowingDto(o))
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * 팔로워 조회 API -> 모든 캐릭터의 팔로잉
     */
    @GetMapping("/er/all")
    public ResponseEntity<List<ResFollowerDto>> findFollower(@Login Long memberId) {
        Character character = memberService.findOne(memberId).getCharacter();
        List<Follow> follows = followService.findAllWithFollower(character.getId());

        if(follows.isEmpty()) {
            return null;
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
