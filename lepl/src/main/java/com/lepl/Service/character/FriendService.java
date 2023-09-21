package com.lepl.Service.character;

import com.lepl.Repository.character.FriendRepository;
import com.lepl.domain.character.Friend;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) //일기 모드
@RequiredArgsConstructor
public class FriendService {

    @Autowired
    private FriendRepository friendRepository;

    /*
        save, findOne, findAll, remove
     */

    //친구 팔로우
    @Transactional //쓰기 모드
    public void save(Friend friend) {
        friendRepository.save(friend);
    }

    //특정 친구 검색
    public Friend findOne(Long id) {
        return friendRepository.findOne(id);
    }

    //전체 친구 검색(팔로잉 목록)
    public List<Friend> findAll() {
        return friendRepository.findAll();
    }

    //친구 팔로우 취소
    @Transactional //쓰기 모드
    public void remove(Long id) {
        friendRepository.remove(id);
    }
}
