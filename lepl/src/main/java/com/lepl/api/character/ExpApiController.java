package com.lepl.api.character;

import com.lepl.Service.character.CharacterService;
import com.lepl.Service.character.ExpService;
import com.lepl.Service.member.MemberService;
import com.lepl.Service.task.ListsService;
import com.lepl.Service.task.TaskService;
import com.lepl.api.argumentresolver.Login;
import com.lepl.domain.character.Character;
import com.lepl.domain.character.Exp;
import com.lepl.domain.member.Member;
import com.lepl.domain.task.Task;
import com.lepl.domain.task.TaskStatus;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/exp")
@Slf4j
public class ExpApiController {

    private final ExpService expService;
    private final TaskService taskService;
    private final ListsService listsService;
    private final MemberService memberService;
    private final CharacterService characterService;

    /*
    * 경험치량 조회 API - 누적, 현재, 레벨
    * */
    @GetMapping("/all")
    public Exp findOneWithMember(@Login Long memberId) {
        Member member = expService.findOneWithMember(memberId);

        if(member == null) {
            return null;
        }

        return member.getCharacter().getExp();
    }

    /*
     * 경험치 업데이트 - 일반 일정 완료
     * List 형태로 받기
     * */
    @PostMapping("/tasks")
    public ResponseEntity<String> expTask(@Login Long memberId, @RequestBody List<TaskDto> taskDtos) {
        log.debug("exp/tasks 입장");

        Member member = expService.findOneWithMember(memberId); // 영속 exp
        if(member == null) {
            return null;
        }

        //화폐 업데이트를 위해 사용
        Character character = member.getCharacter();
        Long money = character.getMoney();

        Exp exp = member.getCharacter().getExp();
        Long pointTimer = 0L;
        Long pointTask = 0L;
        log.debug("기존 exp? : {}", exp.getExpAll());

        List<Task> tasks = new ArrayList<>();
        for(TaskDto taskDto : taskDtos) {
            Task t = taskService.findOne(taskDto.getTaskId());
            tasks.add(t);
        }

        for(Task task : tasks) {
            if(task.getTaskStatus().getCompletedStatus()) {
                continue;
            }
            if(!task.getTaskStatus().getTimerOnOff()) {
                TaskStatus taskStatus = TaskStatus.createTaskStatus(true, false);
                taskService.updateStatus(task, taskStatus, 0l);
                pointTask++;
                log.debug("확인용 pointTask : {}", pointTask);
            }
        }

        //경험치 업데이트
        expService.update(exp, pointTask, pointTimer); //더티 체킹
        characterService.updateCoin(money + pointTask); //더티 체킹

        return ResponseEntity.status(HttpStatus.OK).body("일정을 완료하였습니다."); //200
    }

    /*
    * 경험치 업데이트 - 일정 타이머 종료
    * 1개씩 받기
    * */
    @PostMapping("/timers")
    public ResponseEntity<String> expTimer(@Login Long memberId, @RequestBody TimerDto timerDto) {
        Member member = expService.findOneWithMember(memberId);
        Task task = taskService.findOne(timerDto.getTaskId());

        if(member == null || task == null) {
            return null;
        }

        //화폐 업데이트를 위해 사용
        Character character = member.getCharacter();
        Long money = character.getMoney();

        Exp exp = member.getCharacter().getExp();
        Long pointTimer = 0L;
        Long pointTask = 0L;

        //이미 이전에 완료했던 일정은 아무처리 없이 null
        if(task.getTaskStatus().getCompletedStatus()) {
            return null;
        }
        if(!task.getTaskStatus().getTimerOnOff()) {
            log.debug("첫 타이머 종료(일정 완료)");

            Date start = Timestamp.valueOf(task.getStartTime());
            Date end = Timestamp.valueOf(task.getEndTime());
            Long remainTime = (end.getTime()-start.getTime())-timerDto.getUseTime(); //잔여 시간
            TaskStatus taskStatus;

            if(remainTime <= 0) { //잔여 시간 0시간 이하
                taskStatus = TaskStatus.createTaskStatus(true, true);
                remainTime = 0L;
            } else { //잔여시간 0시간 초과
                taskStatus = TaskStatus.createTaskStatus(false, true);
            }
            Long timerAllUseTime = task.getLists().getTimerAllUseTime() + timerDto.getUseTime(); // 타이머총사용시간 - 밀리세컨 단위
            Long curTime =task.getLists().getCurTime() + timerDto.getUseTime(); // 현재시간 - 밀리세컨 단위

            Long testTime = remainTime; // 디버깅용 hour, minute, second 계산 -> 주석처리 할 것
            Long hour = testTime/(60*60*1000);
            testTime %= (60*60*1000);
            Long minute = testTime / (60*1000);
            testTime %= (60*1000);
            Long second = testTime / (1000);
            log.debug("remainTime -> {}:{}:{}", hour, minute, second);

            if(curTime/(60*60*1000) != 0) {
                Long expTime = curTime/(60*60*1000);
                curTime = curTime%(60*60*1000);
                pointTimer = expTime; // 경험치용 시간
            }

            taskService.updateStatus(task, taskStatus, remainTime); //더티 체킹
            listsService.updateTime(task.getLists(), timerAllUseTime, curTime); //더티 처켕
        }

        //경험치 업데이트
        expService.update(exp, pointTask, pointTimer); //더티 체킹
        characterService.updateCoin(money + pointTask + pointTimer); //더티 체킹

        return ResponseEntity.status(HttpStatus.OK).body("일정을 완료하였습니다."); //200
    }


    // DTO
    @Getter
    static class TaskDto {
        private Long taskId;
    }
    @Getter
    static class TimerDto {
        private Long taskId;
        private Long useTime; // 밀리세컨 단위
    }

    @PostConstruct
    @Transactional
    public void init() {
        log.info("PostConstruct 테스트 " );
        // 테스트용 데이터 삽입

    }
}
