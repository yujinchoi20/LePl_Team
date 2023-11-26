package com.lepl.api.character;

import com.lepl.Service.character.CharacterService;
import com.lepl.Service.character.ExpService;
import com.lepl.Service.member.MemberService;
import com.lepl.Service.task.ListsService;
import com.lepl.Service.task.TaskService;
import com.lepl.domain.character.Exp;
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

import static com.lepl.util.Messages.SESSION_NAME_LOGIN;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ExpApiController.class)
class ExpApiControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean // 가짜 객체
    ExpService expService;
    @MockBean
    TaskService taskService;
    @MockBean
    ListsService listsService;
    @MockBean
    MemberService memberService;
    @MockBean
    CharacterService characterService;
    static MockHttpSession s;
    static final Long MEMBER_ID = 1L;
    static final Long TASK_ID = 1L;
    static final Long TASK_ID2 = 2L;

    @BeforeEach
    public void 로그인_세션() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SESSION_NAME_LOGIN, MEMBER_ID); // 회원 인증 인터셉터 통과 + @Login memberId
        s = session;
    }

    /**
     * findOneWithMember, successTasks, successTimer
     */

    @Test
    public void 경험치_조회() throws Exception {
        // given
        Exp exp = Exp.createExp(15L, 5L, 2L);
        Exp exp2 = null;

        // when
        when(expService.findOneWithMember(MEMBER_ID)).thenReturn(exp);
        mockMvc.perform(
                        get("/api/v1/exp")
                                .session(s)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                )
                .andExpect(status().isOk())
                .andDo(print());
        when(expService.findOneWithMember(MEMBER_ID)).thenReturn(exp2);
        mockMvc.perform(
                        get("/api/v1/exp")
                                .session(s)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                )
                .andExpect(status().isNotFound())
                .andDo(print());

        // then
        verify(expService, times(2)).findOneWithMember(MEMBER_ID);
    }

    @Test
    public void 경험치_업데이트_일정완료() throws Exception {
        // given
        String content = "[{\"taskId\":1}, {\"taskId\":2}]";
        Exp exp = Exp.createExp(2L, 2L, 1L);
        Exp exp2 = Exp.createExp(4L,4L,1L);
        TaskStatus taskStatus = TaskStatus.createTaskStatus(false, false);
        Task.createTask("test", LocalDateTime.now(), LocalDateTime.now(), taskStatus);
        Task t1 = Task.createTask("test", LocalDateTime.now(), LocalDateTime.now(), taskStatus);
        Task t2 = Task.createTask("test", LocalDateTime.now(), LocalDateTime.now(), taskStatus);

        // when
        when(expService.findOneWithMember(MEMBER_ID)).thenReturn(exp);
        when(taskService.findOne(TASK_ID)).thenReturn(t1);
        when(taskService.findOne(TASK_ID2)).thenReturn(t2);
        when(expService.update(exp, 2L, 0L)).thenReturn(exp2);
        mockMvc.perform(
                        post("/api/v1/exp/tasks")
                                .session(s)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .content(content)
                )
                .andExpect(status().isOk())
                .andDo(print());

        // then
        verify(expService).findOneWithMember(any());
        verify(taskService, times(2)).findOne(any());
        verify(expService).update(any(), any(), any());
    }

    @Test
    public void 경험치_업데이트_타이머완료() throws Exception {
        // given
        String content = "{\"taskId\":1, \"useTime\":7200099}";
        Exp exp = Exp.createExp(0L,0L,1L);
        Exp exp2 = Exp.createExp(5L,5L,1L);
        TaskStatus taskStatus = TaskStatus.createTaskStatus(false, false);
        TaskStatus taskStatus2 = TaskStatus.createTaskStatus(false, true);
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = LocalDateTime.now();
        Task task = Task.createTask("테스트 경험치 업데이트 첫 타이머 종료", start, end, taskStatus);
        Task task2 = Task.createTask("테스트 경험치 업데이트 처음 이후 타이머 종료", start, end, taskStatus);
        Member member = Member.createMember("111", "경험치 테스트");
        Lists lists = Lists.createLists(member, LocalDateTime.now(), new ArrayList<>());
        task.setLists(lists);

        // when
        when(expService.findOneWithMember(MEMBER_ID)).thenReturn(exp);
        when(taskService.findOne(TASK_ID)).thenReturn(task);
        when(expService.update(any(), any(), any())).thenReturn(exp2);
        mockMvc.perform(
                        post("/api/v1/exp/timer")
                                .session(s)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .content(content)
                )
                .andExpect(status().isOk())
                .andDo(print());

        // then
        verify(taskService).updateStatus(any(), any(), any(), any());
        verify(listsService).updateTime(any(), any(), any());
    }

}