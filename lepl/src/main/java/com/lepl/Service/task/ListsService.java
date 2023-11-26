package com.lepl.Service.task;


import com.lepl.Repository.task.ListsRepository;
import com.lepl.domain.task.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ListsService {
    private final ListsRepository listsRepository;

    /**
     * findAllWithMemberTask, findByDateWithMemberTask, join, findByCurrent, findOneWithMemberTask, remove
     */

    public Lists findOne(Long id) {
        return listsRepository.findOne(id);
    }

    public List<Lists> findOneWithTask(Long id) {
        return listsRepository.findOneWithTask(id);
    }

    public List<Lists> findByDate(LocalDateTime start, LocalDateTime end) {
        return listsRepository.findByDate(start, end);
    }

    public List<Lists> findAll() {
        return listsRepository.findAll();
    }

    public List<Lists> findAllWithTask() {
        return listsRepository.findAllWithTask();
    }


    //////////////////// 실제 사용은 아래 ////////////////////////

    @Transactional // 쓰기모드
    public void join(Lists lists) {
        listsRepository.save(lists);
    }

    @Transactional // 쓰기모드
    public void remove(Lists lists) {
        listsRepository.remove(lists);
    }

    public List<Lists> findAllWithMemberTask(Long memberId) {
        return listsRepository.findAllWithMemberTask(memberId);
    }

    public List<Lists> findByDateWithMemberTask(Long memberId, LocalDateTime start, LocalDateTime end) {
        return listsRepository.findByDateWithMemberTask(memberId, start, end);
    }

    public Lists findByCurrent(Long memberId, LocalDateTime curDate) {
        return listsRepository.findByCurrent(memberId, curDate);
    }

    public Lists findOneWithMemberTask(Long memberId, Long listsId) {
        return listsRepository.findOneWithMemberTask(memberId, listsId);
    }

    @Transactional // 쓰기모드 - 타이머 종료..
    public void updateTime(Lists lists, Long timerAllUseTime, Long curTime) {
        lists.updateTime(timerAllUseTime, curTime);
    }

}