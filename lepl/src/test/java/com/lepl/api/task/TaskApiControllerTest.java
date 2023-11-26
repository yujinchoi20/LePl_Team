package com.lepl.api.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lepl.Service.member.MemberService;
import com.lepl.Service.task.ListsService;
import com.lepl.Service.task.TaskService;
import com.lepl.Service.task.TaskStatusService;
import com.lepl.domain.member.Member;
import com.lepl.domain.task.Lists;
import com.lepl.domain.task.Task;
import com.lepl.domain.task.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.lepl.util.Messages.SESSION_NAME_LOGIN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TaskApiController.class)
class TaskApiControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    TaskService taskService;
    @MockBean
    TaskStatusService taskStatusService;
    @MockBean
    ListsService listsService;
    @MockBean
    MemberService memberService;
    static MockHttpSession s; // 전역
    static final Long MEMBER_ID = 1L;
    static final Long TASK_ID = 1L;

    @BeforeEach
    public void 로그인_세션() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SESSION_NAME_LOGIN, MEMBER_ID); // 회원 인증 인터셉터 통과 + @Login memberId
        s = session;
    }

    /**
     * create, delete, update
     */
    @Test
    public void 일정_추가() throws Exception {
        // given
        LocalDateTime st = LocalDateTime.now();
        LocalDateTime en = LocalDateTime.now();
        Map<String, Object> map = new HashMap<>();
        map.put("content", "일정추가 테스트");
        map.put("startTime", st);
        map.put("endTime", en);
        ObjectMapper obj = new ObjectMapper();
        String content = obj.registerModule(new JavaTimeModule()).writeValueAsString(map); // Jackson 2.8.0 이전 버전에서는 JavaTimeModule 을 써야 에러 해결(직렬화 에러)
        Lists lists = Lists.createLists(Member.createMember("1", "1"), st, new ArrayList<>());

        // when
        when(listsService.findByCurrent(MEMBER_ID, st)).thenReturn(lists);
        mockMvc.perform(
                        post("/api/v1/tasks/new")
                                .session(s)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .content(content)
                )
                .andExpect(status().isCreated())
                .andDo(print());

        // then
        verify(listsService).join(any());
        verify(taskStatusService).join(any());
        verify(taskService).join(any());
    }

    @Test
    public void 일정_삭제() throws Exception {
        // given
        String content = "{\"taskId\":1}";
        Task task = Task.createTask("test", LocalDateTime.now(), LocalDateTime.now(), TaskStatus.createTaskStatus(false, false));

        // when
        when(taskService.findOneWithMember(MEMBER_ID, TASK_ID)).thenReturn(task);
        mockMvc.perform(
                        post("/api/v1/tasks/member/delete")
                                .session(s)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .content(content)
                )
                .andExpect(status().isOk())
                .andDo(print());

        // then
        verify(taskService).remove(task);
    }

    @Test
    public void 일정_수정() throws Exception {
        // given
        LocalDateTime st = LocalDateTime.now();
        LocalDateTime en = LocalDateTime.now();
        Map<String, Object> map = new HashMap<>();
        map.put("content", "일정추가 테스트 수정!");
        map.put("startTime", st);
        map.put("endTime", en);
        map.put("taskId", TASK_ID);
        ObjectMapper obj = new ObjectMapper();
        String content = obj.registerModule(new JavaTimeModule()).writeValueAsString(map); // Jackson 2.8.0 이전 버전에서는 JavaTimeModule 을 써야 에러 해결(직렬화 에러)
        Task task = Task.createTask("test", LocalDateTime.now(), LocalDateTime.now(), TaskStatus.createTaskStatus(false, false));


        // when
        when(taskService.findOneWithMember(MEMBER_ID, TASK_ID)).thenReturn(task);
        mockMvc.perform(
                        post("/api/v1/tasks/member/update")
                                .session(s)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .content(content)
                )
                .andExpect(status().isOk())
                .andDo(print());

        // then
        verify(taskService).update(any(), any(), any(), any());
    }
}