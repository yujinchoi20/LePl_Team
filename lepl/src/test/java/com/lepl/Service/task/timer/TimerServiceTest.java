package com.lepl.Service.task.timer;

import com.lepl.Service.task.TaskService;
import com.lepl.domain.task.Task;
import com.lepl.domain.task.timer.Timer;
import com.lepl.domain.task.timer.TimerStatus;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class TimerServiceTest {
    @Autowired
    TimerService timerService;
    @Autowired
    TaskService taskService;

    /**
     * save, findOne, findAll
     */
    @Test
    public void save_find() throws Exception {
        // given
        //        LocalDateTime testDate = LocalDateTime.of(2022, Month.APRIL, 23, 12, 30); // test 날짜
//        LocalDateTime testDate2 = LocalDateTime.of(2022, Month.JANUARY, 1, 0, 0); // test 날짜 1월.1일.0시.0분
//        LocalDateTime testDate3 = LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0); // test 날짜 1월.1일.0시.0분
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.of(2023, Month.MAY, 28, 10, 30);

        Task task = new Task();
        task.setContent("test@@");
        Timer timer = Timer.createTimer(task, start, end, TimerStatus.ALLOW);
        Timer timer2 = Timer.createTimer(task, start, end, TimerStatus.FOCUS);

        // when
        taskService.join(task); // 먼저 persist 되어야함.
        timerService.join(timer);
        timerService.join(timer2);
        Timer findTimer = timerService.findOne(timer.getId());
        List<Timer> findTimers = timerService.findAll();

        // then
        Assertions.assertEquals(timer, findTimer);
        for(Timer t : findTimers) {
            System.out.println(t.getTask().getContent());
            System.out.println(t.getStartTime());
            System.out.println(t.getEndTime());
            System.out.println(t.getTimerStatus());
            System.out.println();
        }
    }
}