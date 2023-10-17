package com.lepl.Service.character;

import com.lepl.Repository.character.FollowRepository;
import com.lepl.domain.character.Follow;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) //일기 모드
@RequiredArgsConstructor
public class FollowService {

    @Autowired
    private FollowRepository followRepository;

    /*
        save, findOne, findAll, remove
     */

    //친구 팔로우
    @Transactional //쓰기 모드
    public void save(Follow follow) {
        followRepository.save(follow);
    }

    //특정 친구 검색
    public Follow findOne(Long id) {
        return followRepository.findOne(id);
    }

    //전체 친구 검색(팔로잉 목록)
    public List<Follow> findAll() {
        return followRepository.findAll();
    }

    //친구 팔로우 취소
    @Transactional //쓰기 모드
    public void remove(Follow follow) {
        followRepository.remove(follow);
    }

    public List<Follow> findAllWithFollowing(Long characterId) { return followRepository.findAllWithFollowing(characterId); }
    public List<Follow> findAllWithFollower(Long characterId) { return followRepository.findAllWithFollower(characterId); }
}
