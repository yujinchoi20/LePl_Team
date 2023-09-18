package com.lepl.Service.task.timer;

import com.lepl.Repository.task.timer.TimerRepository;
import com.lepl.domain.task.timer.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor // 생성자 주입 + 엔티티 매니저(안씀)
@Transactional(readOnly = true) // 읽기 모드
public class TimerService {
    private final TimerRepository timerRepository;

    /**
     * save, findOne, findAll
     */
    @Transactional // 쓰기 모드
    public void join(Timer timer) {
        timerRepository.save(timer);
    }
    public Timer findOne(Long id) {
        return timerRepository.findOne(id);
    }
    public List<Timer> findAll() {
        return timerRepository.findAll();
    }
}
