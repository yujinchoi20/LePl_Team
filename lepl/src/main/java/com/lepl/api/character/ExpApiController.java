package com.lepl.api.character;


import com.lepl.Service.character.CharacterService;
import com.lepl.Service.character.ExpService;
import com.lepl.Service.member.MemberService;
import com.lepl.Service.task.ListsService;
import com.lepl.Service.task.TaskService;
import com.lepl.api.argumentresolver.Login;
import com.lepl.domain.character.Character;
import com.lepl.domain.character.Exp;
import com.lepl.domain.character.dto.CalTimeDto;
import com.lepl.domain.member.Member;
import com.lepl.domain.task.Lists;
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
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/exp")
public class ExpApiController {
    private final ExpService expService;
    private final TaskService taskService;
    private final ListsService listsService;

    private final MemberService memberService;
    private final CharacterService characterService;

    /**
     * findOneWithMember, successTasks, successTimer
     */

    /**
     * 경험치량 조회 API - 누적, 현재, 레벨 제공
     */
    @GetMapping()
    public ResponseEntity<ResExpDto> findOneWithMember(@Login Long memberId) {
        Exp exp = expService.findOneWithMember(memberId);
        if (exp == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        ResExpDto result = new ResExpDto(exp);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * 경험치 업데이트 API - 일반 일정 완료 (List 형태이므로 여러개 업데이트 가능)
     * 입력 -> JSON
     * taskId 필수
     */
    @PostMapping("/tasks")
    public ResponseEntity<ResExpDto> successTasks(@Login Long memberId, @RequestBody List<ReqTaskDto> request) {
        log.debug("exp/tasks 입장");

        Member member = memberService.findOne(memberId); //캐릭터 화폐 업데이트를 위함
        Exp exp = expService.findOneWithMember(memberId); // 영속 exp
        if (exp == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        //화폐 업데이트를 위해 사용
        Character character = member.getCharacter();
        Long money = character.getMoney();

        log.debug("기존 exp? : {}", exp.getExpAll());
        Long pointTimer = 0L;
        Long pointTask = 0L;
        // 완료 Task 불러오기
        List<Task> tasks = new ArrayList<>();
        for (ReqTaskDto taskDto : request) {
            Task t = taskService.findOne(taskDto.getTaskId());
            tasks.add(t);
        }

        // pointTask 계산
        for (Task task : tasks) {
            if (task.getTaskStatus().getCompletedStatus()) continue; // 이미 이전에 완료했던 일정이라 pass
            if (!task.getTaskStatus().getTimerOnOff()) { // 타이머가 아닌지 추가 점검 -> 타이머가 아니라면
                taskService.updateStatus(task, true, false, 0l);
                pointTask++;
                log.debug("확인용 pointTask : {}", pointTask);
            }
        }

        // Exp 업데이트
        exp = expService.update(exp, pointTask, pointTimer); // 더티체킹
        characterService.updateCoin(money + pointTask, character.getId()); //더티 체킹

        return ResponseEntity.status(HttpStatus.OK).body(new ResExpDto(exp)); // 200
    }

    /**
     * 경험치 업데이트 API - 일정 타이머 종료 (타이머 특성상 1개씩 완료)
     * 입력 -> JSON
     * taskId, useTime 필수 -> 밀리세컨 단위
     */
    @PostMapping("/timer")
    public ResponseEntity<ResExpDto> successTimer(@Login Long memberId, @RequestBody ReqTimerDto request) {
        Member member = memberService.findOne(memberId); //캐릭터 화폐 업데이트를 위함
        Exp exp = expService.findOneWithMember(memberId); // 영속 exp
        Task task = taskService.findOne(request.getTaskId()); // 완료 Task 불러오기

        if (exp == null || task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404
        }
        if (task.getTaskStatus().getCompletedStatus()) { // 이미 이전에 완료했던 일정은 계산 X
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(new ResExpDto(exp)); // 208
        }

        //화폐 업데이트를 위해 사용
        Character character = member.getCharacter();
        Long money = character.getMoney();

        // pointTimer 계산전 init
        Long timerAllUseTime = task.getLists().getTimerAllUseTime() + request.getUseTime(); // 타이머총사용시간 단위 : 밀리세컨 단위
        Long curTime = task.getLists().getCurTime() + request.getUseTime(); // 현재시간 단위 : 밀리세컨 단위
        CalTimeDto calTimeDto = new CalTimeDto(timerAllUseTime, curTime);

        // pointTimer 계산
        if (!task.getTaskStatus().getTimerOnOff()) { // 첫 타이머 종료
            log.debug("첫 타이머 종료(일정완료)");

            // 일정상태{타이머상태ON}, 일정{잔여시간}, 리스트{총사용시간}, 리스트{현재시간} 기록
            // 잔여시간 계산 -> 처음 계산에는 end - start 가 필요
            Date end = Timestamp.valueOf(task.getEndTime());
            Date start = Timestamp.valueOf(task.getStartTime());
            Long remainTime = (end.getTime() - start.getTime()) - request.getUseTime(); // 잔여시간 단위 : 밀리세컨
            calTimeDto.setRemainTime(remainTime);
        } else { // 처음이후 타이머 종료
            log.debug("처음이후 타이머 종료(일정완료)");
            // 잔여시간 계산
            Long remainTime = task.getRemainTime() - request.getUseTime(); // 잔여시간 - 밀리세컨 단위
            calTimeDto.setRemainTime(remainTime);
        }
        // 시간 계산
        calTimeDto.calTime();
        // Exp 업데이트
        taskService.updateStatus(task, calTimeDto.getCompleted(), calTimeDto.getTimerOnOff(), calTimeDto.getRemainTime()); // 더티체킹
        listsService.updateTime(task.getLists(), calTimeDto.getTimerAllUseTime(), calTimeDto.getCurTime()); // 더티체킹
        exp = expService.update(exp, calTimeDto.getPointTask(), calTimeDto.getPointTimer()); // 더티체킹
        characterService.updateCoin(money + calTimeDto.getPointTask() + calTimeDto.getPointTimer(), character.getId()); //더티 체킹

        return ResponseEntity.status(HttpStatus.OK).body(new ResExpDto(exp)); // 200
    }

    // DTO
    @Getter
    static class ReqTaskDto {
        private Long taskId;
    }

    @Getter
    static class ReqTimerDto {
        private Long taskId;
        private Long useTime; // 밀리세컨 단위
    }

    @Getter
    static class ResExpDto {
        private Long expAll;
        private Long expValue;
        private Long level;

        public ResExpDto(Exp exp) {
            expAll = exp.getExpAll();
            expValue = exp.getExpValue();
            level = exp.getLevel();
        }
    }

    //    @PostConstruct
    @Transactional
    public void init() {
        log.info("PostConstruct 테스트 ");
        // 테스트용 데이터 삽입
        for(int u=0; u<50000; u++) {
            Exp exp = Exp.createExp(5L, 5L, 1L);
            expService.join(exp);
            Character character = Character.createCharacter(exp, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
            characterService.join(character);

            Member member = Member.createMember("123"+(u+4), "사용자"+(u+1));
            member.setCharacter(character);
            memberService.join(member);

            // Task 3개정도
            LocalDateTime today = LocalDateTime.now();
            Lists lists = Lists.createLists(member, today, new ArrayList<>());
            listsService.join(lists); // 먼저 lists init
            for (long i = 1; i <= 3; i++) {
                LocalDateTime end = LocalDateTime.now();
                LocalDateTime start = LocalDateTime.of(2023, Month.SEPTEMBER, 21, 20, 30);
                TaskStatus taskStatus = TaskStatus.createTaskStatus(false, false); // default
                Task t = Task.createTask("test", start, end, taskStatus, 0l);
                t.setLists(lists);
                taskService.join(t);
            }

            // 번외) 시간 계산 테스트용
            LocalDateTime startTime = LocalDateTime.of(2023, Month.OCTOBER, 13, 13, 10);
            LocalDateTime endTime = LocalDateTime.of(2023, Month.OCTOBER, 13, 15, 10);
            Date end = Timestamp.valueOf(endTime);
            Date start = Timestamp.valueOf(startTime);
            Long diff = (end.getTime() - start.getTime());
            log.debug("시간 계산 테스트용 밀리세컨단위 : {}", diff);
        }
    }

    //    @PostConstruct
//    @Transactional
    public void init2() {
        log.info("PostConstruct 테스트 ");
        // 테스트용 데이터 삽입
        for(int u=0; u<5000; u++) {
            // Lists 1000개
            Member member = memberService.findOne(1L); // "1234" uid
//            LocalDateTime today = LocalDateTime.now();
            LocalDateTime today = LocalDateTime.of(2023, 11, 20, 20, 30);
            Lists lists = Lists.createLists(member, today, new ArrayList<>());
            listsService.join(lists); // 먼저 lists init
            for (long i = 1; i <= 3; i++) {
//                LocalDateTime end = LocalDateTime.now();
                LocalDateTime end = LocalDateTime.of(2023, 11, 20, 20, 30);
                LocalDateTime start = LocalDateTime.of(2023, 11, 20, 10, 30);
                TaskStatus taskStatus = TaskStatus.createTaskStatus(false, false); // default
                Task t = Task.createTask("test", start, end, taskStatus, 0l);
                t.setLists(lists);
                taskService.join(t);
            }

            // 번외) 시간 계산 테스트용
            LocalDateTime startTime = LocalDateTime.of(2023, Month.OCTOBER, 13, 13, 10);
            LocalDateTime endTime = LocalDateTime.of(2023, Month.OCTOBER, 13, 15, 10);
            Date end = Timestamp.valueOf(endTime);
            Date start = Timestamp.valueOf(startTime);
            Long diff = (end.getTime() - start.getTime());
            log.debug("시간 계산 테스트용 밀리세컨단위 : {}", diff);
        }
    }
}