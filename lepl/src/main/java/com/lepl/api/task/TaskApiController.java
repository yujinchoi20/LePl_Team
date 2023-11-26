package com.lepl.api.task;

import com.lepl.Service.member.MemberService;
import com.lepl.Service.task.ListsService;
import com.lepl.Service.task.TaskService;
import com.lepl.Service.task.TaskStatusService;
import com.lepl.api.argumentresolver.Login;
import com.lepl.domain.member.Member;
import com.lepl.domain.task.Lists;
import com.lepl.domain.task.Task;
import com.lepl.domain.task.TaskStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static com.lepl.util.Messages.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/tasks")
public class TaskApiController {
    private final TaskService taskService;
    private final TaskStatusService taskStatusService;
    private final ListsService listsService;
    private final MemberService memberService;

    /**
     * create, delete, update
     */

    /**
     * 일정 추가
     * 요청 형식(json) : CreateTaskRequestDto
     */
    @PostMapping(value = "/new")
    public ResponseEntity<String> create(@Login Long memberId, @RequestBody CreateTaskRequestDto request) {
        Lists lists = listsService.findByCurrent(memberId, request.startTime); // db 에 startTime 인 lists 있는지 먼저 조회
        if (lists == null) { // null 인 경우 새로생성
            Member member = memberService.findOne(memberId);
            lists = Lists.createLists(member, request.startTime, new ArrayList<Task>());
        }

        LocalDateTime start = request.startTime;
        LocalDateTime end = request.endTime;
        LocalDateTime remain = end.minusHours(start.getHour()).minusMinutes(start.getMinute()); //모든 일정이 하루안에 끝난다고 가정
        request.remainTime = Long.valueOf(((remain.getHour()*60) + remain.getMinute()));
        //분으로 표시 = hour*60 + minutes

        TaskStatus taskStatus = TaskStatus.createTaskStatus(false, false);
        Task task = Task.createTask(request.content, request.startTime, request.endTime, taskStatus, request.remainTime);
        lists.addTask(task); // 일정 추가

        listsService.join(lists);
        taskStatusService.join(taskStatus);
        taskService.join(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(SUCCESS_TASK);
    }

    /**
     * 일정 삭제
     * 요청 형식(json) : DeleteTaskRequestDto
     */
    @PostMapping(value = "/member/delete")
    public ResponseEntity<String> delete(@Login Long memberId, @RequestBody DeleteTaskRequestDto request) {
        Task task = taskService.findOneWithMember(memberId, request.getTaskId());
        if (task == null) {
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(VALID_TASK); // 208
        }
        taskService.remove(task);
        return ResponseEntity.status(HttpStatus.OK).body(SUCCESS_TASK_DELETE); // 200
    }

    /**
     * 일정 수정
     * 요청 형식(json) : UpdateTaskRequestDto
     */
    @PostMapping(value = "/member/update")
    public ResponseEntity<String> update(@Login Long memberId, @RequestBody UpdateTaskRequestDto request) {
        Task task = taskService.findOneWithMember(memberId, request.getTaskId());
        if (task == null) {
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(VALID_TASK); // 208
        }
        taskService.update(task, request.content, request.startTime, request.endTime); // 변경 감지
        return ResponseEntity.status(HttpStatus.OK).body(SUCCESS_TASK_UPDATE); // 200
    }


    // DTO => 엔티티 외부노출 금지 + 필요한것만 담아서 반환할 수 있어서 효과적
    @Getter
    static class CreateTaskRequestDto {
        private String content;
        private LocalDateTime startTime;
        private LocalDateTime endTime;

        private Long remainTime;
    }

    @Getter
    static class DeleteTaskRequestDto {
        private Long taskId;
    }

    @Getter
    static class UpdateTaskRequestDto {
        private Long taskId;
        private String content;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
    }
}