package com.lepl.api.task;

import com.lepl.Service.task.ListsService;
import com.lepl.api.argumentresolver.Login;
import com.lepl.domain.task.Lists;
import com.lepl.domain.task.Task;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/lists")
public class ListsApiController {
    private final ListsService listsService;

    /**
     * 일정 조회 1과 2는 프론트에서 사용할 일 없음. 프론트는 일정 조회 3과 4 사용
     */
    
    /**
     * 일정 조회(1) - 모든 Lists(=하루단위 일정모음) 조회
     */
    @GetMapping(value = "/all")
    public List<ListsDto> findAllWithTask() {
        List<Lists> lists = listsService.findAllWithTask();
        List<ListsDto> result = lists.stream()
                .map(o -> new ListsDto(o))
                .collect(Collectors.toList());
        return result;
    }
    /**
     * 일정 조회(2) - 특정 Lists(=하루단위 일정모음) 조회 - listsId 를 통해서
     */
    @GetMapping(value = "/{listsId}")
    public List<TaskDto> findOneWithTask(@PathVariable("listsId") Long listsId) {
        List<Lists> lists = listsService.findOneWithTask(listsId);
        if(lists.isEmpty()) return null;
        List<TaskDto> result = lists.get(0).getTasks().stream()
                .map(o -> new TaskDto(o))
                .collect(Collectors.toList());
        return result;
    }

    /**
     * 일정 조회(3) - 모든 Lists(=하루단위 일정모음) 조회 -> 해당 회원꺼만
     */
    @GetMapping(value = "/member/all")
    public List<ListsDto> findAllWithMemberTask(@Login Long memberId) {
        List<Lists> lists = listsService.findAllWithMemberTask(memberId);
        List<ListsDto> result =lists.stream()
                .map(o -> new ListsDto(o))
                .collect(Collectors.toList());
        return result;
    }
    /**
     * 일정 조회(4) - 날짜범위로 Lists(=하루단위 일정모음) 조회 -> 해당 회원꺼만
     * 하루, 한달, 1년 등등 원하는 날짜 범위만큼 사용 가능
     */
    @PostMapping(value = "/member/date")
    public List<ListsDto> findByDateWithMemberTask(@Login Long memberId, @RequestBody CreateListsRequestDto request) {
        List<Lists> lists = listsService.findByDateWithMemberTask(memberId, request.startTime, request.endTime);
        List<ListsDto> result =lists.stream()
                .map(o -> new ListsDto(o))
                .collect(Collectors.toList());
        return result;
    }

    /**
     * 일정 삭제 => memberId 까지 확인해서 안정성을 높이겠음. + cascade 사용
     */
    @PostMapping(value = "/member/delete")
    public ResponseEntity<String> delete(@Login Long memberId, @RequestBody DeleteRequestDto request) {
        List<Lists> lists = listsService.findOneWithMemberTask(memberId, request.getListsId());
        if(lists.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("이미 삭제된 일정입니다."); // 404
        listsService.remove(lists.get(0));
        return ResponseEntity.status(HttpStatus.OK).body("해당 일정이 삭제되었습니다."); // 200
    }


    // DTO
    @Getter
    static class ListsDto {
        private Long listsId;
        private LocalDateTime listsDate; // 등록 날짜
        private List<TaskDto> listsTasks;
        public ListsDto(Lists lists) { // lazy 강제 초기화
            listsId = lists.getId();
            listsDate = lists.getListsDate();
            listsTasks = lists.getTasks().stream()
                    .map(o -> new TaskDto(o))
                    .collect(Collectors.toList());
        }
    }
    @Getter
    static class TaskDto {
        private Long taskId;
        private String content;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private Boolean completedStatus;
        private Boolean timerOnOff;
        public TaskDto(Task task) { // lazy 강제 초기화
            taskId = task.getId();
            content = task.getContent();
            startTime = task.getStartTime();
            endTime = task.getEndTime();
            completedStatus = task.getTaskStatus().getCompletedStatus();
            timerOnOff = task.getTaskStatus().getTimerOnOff();
        }
    }

    @Getter
    static class CreateListsRequestDto {
        private LocalDateTime startTime;
        private LocalDateTime endTime;
    }
    @Getter
    static class DeleteRequestDto {
        private Long listsId;
    }
}
