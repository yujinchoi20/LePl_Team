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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/tasks")
public class TaskApiController {
    private final TaskService taskService;
    private final TaskStatusService taskStatusService;
    private final ListsService listsService;
    private final MemberService memberService;

    /**
     * 일정 추가
     * 요청 형식(json) : CreateTaskRequestDto
     */
    @PostMapping(value = "/new")
    public String create(@Login Long memberId, @RequestBody CreateTaskRequestDto request) {
        Lists lists = null;
        List<Lists> listsList = listsService.findByCurrent(memberId, request.startTime); // db 에 기존 lists+member 가 있나 확인 (startTime, id 로 확인)
        // lists 가 없을 경우
        if(listsList.isEmpty()) { 
            Member member = memberService.findOne(memberId);
            lists = Lists.createLists(member, request.startTime, new ArrayList<Task>());
        }
        // lists 가 있을 경우
        else lists = listsList.get(0);
        listsService.join(lists);

        TaskStatus taskStatus = TaskStatus.createTaskStatus(false, false);
        Task task = Task.createTask(request.content, request.startTime, request.endTime, request.remainTime, taskStatus);
        lists.addTask(task); // 일정 추가

        taskStatusService.join(taskStatus);
        taskService.join(task);
        return "일정 등록 성공";
    }


    /**
     * 일정 삭제 => memberId 까지 확인해서 안정성을 높이겠음. 또한, cascade 필요할 수도 있으니 잘 확인
     */
    @PostMapping(value = "/member/delete")
    public ResponseEntity<String> delete(@Login Long memberId, @RequestBody DeleteTaskRequestDto request) {
        List<Task> tasks = taskService.findOneWithMember(memberId, request.getTaskId());
        if(tasks.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("이미 삭제된 일정입니다."); // 404
        taskService.remove(tasks.get(0));
        return ResponseEntity.status(HttpStatus.OK).body("해당 일정이 삭제되었습니다."); // 200
    }

    /**
     * 일정 수정 => memberId 까지 확인해서 안정성을 높이겠음. 또한, cascade 필요할 수도 있으니 잘 확인
     * (변경 감지를 사용해서 데이터를 수정) => 더티체킹
     */
    @PostMapping(value = "/member/update")
    public ResponseEntity<String> update(@Login Long memberId, @RequestBody UpdateTaskRequestDto request) {
        List<Task> tasks = taskService.findOneWithMember(memberId, request.getTaskId());
        if(tasks.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("이미 삭제된 일정입니다."); // 404
        taskService.update(tasks.get(0), request.content, request.startTime, request.endTime); // 변경 감지
        return ResponseEntity.status(HttpStatus.OK).body("해당 일정이 수정되었습니다."); // 200
    }


    // DTO => 엔티티 외부노출 금지 + 필요한것만 담아서 반환할 수 있어서 효과적
    @Getter
    static class CreateTaskRequestDto {
        private String content;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private LocalDateTime remainTime;
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
        private LocalDateTime remainTime;
    }
}
