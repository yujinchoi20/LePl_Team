package com.lepl.api.task.timer;

import com.lepl.Service.task.TaskService;
import com.lepl.Service.task.timer.TimerService;
import com.lepl.api.argumentresolver.Login;
import com.lepl.domain.task.Task;
import com.lepl.domain.task.timer.Timer;
import com.lepl.domain.task.timer.TimerStatus;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/timers")
public class TimerApiController {
    private final TimerService timerService;
    private final TaskService taskService;

    /**
     * Task 에 타이머 추가
     * => 앱에서 타이머 시간 흐르고나서 종료하면 추가. (이미 있는 경우도 새로 추가 - 경험치 계산에 따로 쓰는게 편해보임.)
     */
    @PostMapping("/new")
    public String create(@Login Long memberId, @RequestBody CreateTimerRequestDto request) {
        Task task = taskService.findOne(request.taskId);
        TimerStatus timerStatus = null;
        if(request.status == 0) timerStatus = TimerStatus.ALLOW;
        else timerStatus = TimerStatus.FOCUS;

        Timer timer = Timer.createTimer(task, request.startTime, request.endTime, timerStatus);
        timerService.join(timer);
        return "타이머 등록 완료";
    }

    /**
     * Task 의 타이머 조회
     */
    @GetMapping("/{teskId}")
    public ResponseEntity<List<ResTimerDto>> findTimerWithTask(@PathVariable("taskId") Long taskId) {
        Task task = taskService.findOne(taskId);
        List<Timer> timers = task.getTimers();
        if(timers.isEmpty()) return null;

        List<ResTimerDto> result = timers.stream()
                .map(o -> new ResTimerDto(o))
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // DTO
    @Getter
    static class CreateTimerRequestDto {
        private Long taskId;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private Integer status; // 0 : ALLOW, 1 : FOCUS
    }
    @Getter
    static class ResTimerDto {
        private Long timerId;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private TimerStatus timerStatus; // enum
        public ResTimerDto(Timer timer) {
            this.timerId = timer.getId();
            this.startTime = timer.getStartTime();
            this.endTime = timer.getEndTime();
            this.timerStatus = timer.getTimerStatus();
        }
    }

    @PostConstruct
    @Transactional
    public void init() {
        log.debug("PostConstruct 테스트 " );
        // 테스트용 데이터 삽입
        for(long i = 1 ; i<=3; i++) {
            Task t = new Task();
            taskService.join(t);
        }
    }
}
