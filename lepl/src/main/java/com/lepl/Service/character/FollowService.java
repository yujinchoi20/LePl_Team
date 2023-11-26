package com.lepl.Service.character;

import com.lepl.Repository.character.FollowRepository;
import com.lepl.domain.character.Follow;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // 읽기모드
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;

    /**
     * join, findOne, findAll, remove, findAllWithFollowing, findAllWithFollower
     */
    @Transactional // 쓰기모드
    public Long join(Follow follow) {
        // 1. 중복 검증(필수)
        validateDuplicateFollow(follow);
        // 2. 팔로우 기록
        followRepository.save(follow);
        return follow.getId();
    }

    private void validateDuplicateFollow(Follow follow) {
        Follow findFollow = followRepository.findById(follow.getFollowerId(), follow.getFollowingId());
        if (findFollow != null) {
            // IllegalStateException 예외를 호출
            throw new IllegalStateException("이미 팔로우 요청을 하셨습니다.");
        }
    }

    public Follow findOne(Long followId) {
        return followRepository.findOne(followId);
    }

    public List<Follow> findAll() {
        return followRepository.findAll();
    }

    @Transactional
    public void remove(Follow follow) {
        followRepository.remove(follow);
    }

    public List<Follow> findAllWithFollowing(Long characterId) {
        return followRepository.findAllWithFollowing(characterId);
    }

    public List<Follow> findAllWithFollower(Long characterId) {
        return followRepository.findAllWithFollower(characterId);
    }
}