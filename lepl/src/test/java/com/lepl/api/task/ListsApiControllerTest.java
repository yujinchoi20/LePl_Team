package com.lepl.api.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lepl.Service.task.ListsService;
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
import java.util.List;
import java.util.Map;

import static com.lepl.util.Messages.SESSION_NAME_LOGIN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ListsApiController.class)
class ListsApiControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    ListsService listsService;
    static List<Lists> listsList;
    static MockHttpSession s; // 전역
    static final Long MEMBER_ID = 1L;

    @BeforeEach
    public void 로그인_세션() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SESSION_NAME_LOGIN, MEMBER_ID); // 회원 인증 인터셉터 통과 + @Login memberId
        s = session;
    }

    @BeforeEach
    public void 필요객체_생성() {
        LocalDateTime localDateTime = LocalDateTime.now();
        listsList = new ArrayList<>();
        Member member = Member.createMember("111", "경험치 테스트");
        Lists l1 = Lists.createLists(member, LocalDateTime.now(), new ArrayList<>());
        l1.setId(1L);
        l1.setTimerAllUseTime(5L);
        TaskStatus taskStatus = TaskStatus.createTaskStatus(true, true);
        Task t1 = Task.createTask("test", localDateTime, localDateTime, taskStatus);
        t1.setId(1L);
        l1.addTask(t1);
        listsList.add(l1);
    }

    /**
     * findAllWithMemberTask, findByDateWithMemberTask
     */
    @Test
    public void 회원의_모든_Lists_조회() throws Exception {
        // given

        // when
        when(listsService.findAllWithMemberTask(MEMBER_ID)).thenReturn(listsList);
        mockMvc.perform(
                        get("/api/v1/lists/member/all")
                                .session(s)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                )
                .andExpect(status().isOk())
                .andDo(print());

        // then
        verify(listsService).findAllWithMemberTask(any());
    }

    @Test
    public void 회원의_해당날짜범위의_Lists_조회() throws Exception {
        // given
        LocalDateTime st = LocalDateTime.now();
        LocalDateTime en = LocalDateTime.now();
        Map<String, LocalDateTime> map = new HashMap<>();
        map.put("startTime", st);
        map.put("endTime", en);
        ObjectMapper obj = new ObjectMapper();
        String content = obj.registerModule(new JavaTimeModule()).writeValueAsString(map); // Jackson 2.8.0 이전 버전에서는 JavaTimeModule 을 써야 에러 해결(직렬화 에러)
        // when
        when(listsService.findByDateWithMemberTask(MEMBER_ID, st, en)).thenReturn(listsList);
        mockMvc.perform(
                        post("/api/v1/lists/member/date")
                                .session(s)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .content(content)
                )
                .andExpect(status().isOk())
                .andDo(print());

        // then
        verify(listsService).findByDateWithMemberTask(any(), any(), any());
    }
}