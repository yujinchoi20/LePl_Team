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
     * save, findOne, findByDate, findAll, remove, findByToday
     */

    @Transactional // 쓰기모드
    public void join(Lists lists) {
        listsRepository.save(lists);
    }

    public Lists findOne(Long id) {
        return listsRepository.findOne(id);
    }
    public List<Lists> findOneWithTask(Long id) {
        return listsRepository.findOneWithTask(id);
    }
    public List<Lists> findByDate(LocalDateTime start, LocalDateTime end) {
        return listsRepository.findByDate(start, end);
    }
    public List<Lists> findByCurrent(Long memberId, LocalDateTime curDate) {
        return listsRepository.findByCurrent(memberId, curDate);
    }
    public List<Lists> findAll() {
        return listsRepository.findAll();
    }
    public List<Lists> findAllWithTask() {
        return listsRepository.findAllWithTask();
    }
    public List<Lists> findAllWithMemberTask(Long memberId) {
        return listsRepository.findAllWithMemberTask(memberId);
    }
    public List<Lists> findByDateWithMemberTask(Long memberId, LocalDateTime start, LocalDateTime end) {
        return listsRepository.findByDateWithMemberTask(memberId, start, end);
    }
    public List<Lists> findOneWithMemberTask(Long memberId, Long listsId) {
        return listsRepository.findOneWithMemberTask(memberId, listsId);
    }
    @Transactional // 쓰기모드
    public void remove(Lists lists) {
        listsRepository.remove(lists);
    }

    @Transactional // 쓰기모드, 타이머 누적 시간 및 현재 시간 업데이트
    public void updateTime(Lists lists, Long timerAllUseTime, Long curTime) {
        lists.setTimerAllUseTime(timerAllUseTime);
        lists.setCurTime(curTime);
    }
}
