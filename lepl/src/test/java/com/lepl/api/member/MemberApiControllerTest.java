package com.lepl.api.member;

import com.lepl.Service.character.CharacterService;
import com.lepl.Service.character.ExpService;
import com.lepl.Service.member.MemberService;
import com.lepl.api.member.dto.FindMemberResponseDto;
import com.lepl.domain.character.Character;
import com.lepl.domain.character.Exp;
import com.lepl.domain.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static com.lepl.util.Messages.SESSION_NAME_LOGIN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @SpringBootTest 의 경우 모든 빈을 로드..
 * 컨트롤러만 테스트?? @WebMvcTest 사용
 */
@WebMvcTest(controllers = MemberApiController.class)
class MemberApiControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean // 가짜 객체이므로 실제 동작은 X
    ExpService expService;
    @MockBean
    CharacterService characterService;
    @MockBean
    MemberService memberService;

    /**
     * login, register, logout, findAllWithPage
     */

    @Test
    public void 회원가입() throws Exception {
        // given
        String content = "{\"uid\":\"12345\", \"nickname\":\"회원가입 테스트\"}";
        Member member = Member.createMember("12345", "회원가입 테스트");
        member.setId(1L);

        // when
        when(memberService.join(any())).thenReturn(member);
        mockMvc.perform(
                        post("/api/v1/members/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .content(content)
                )
                .andExpect(status().isCreated()) // 예상 응답
                .andExpect(jsonPath("uid").value("12345")) // 응답 body 의 json 확인
                .andDo(print());

        // then
        verify(memberService).join(any());
        verify(expService).join(any());
        verify(characterService).join(any());
    }

    @Test
    public void 로그인() throws Exception {
        // given
        String content = "{\"uid\":\"123\"}"; // {"name":"value"} 형태로 작성해야 JSON 형태!
        Member member = Member.createMember("123", "test");
        member.setId(1L);

        // when
        when(memberService.findByUid(any())).thenReturn(member);
        mockMvc.perform(
                        post("/api/v1/members/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .content(content)
                )
                .andExpect(status().isOk()) // 예상 응답
                .andDo(print());

        // then
        verify(memberService).findByUid(any());
    }

    @Test
    public void 로그아웃_성공과중복() throws Exception {
        // given
        Member member = Member.createMember("123", "test");
        member.setId(1L);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SESSION_NAME_LOGIN, member.getId());

        // when
        mockMvc.perform(
                        post("/api/v1/members/logout")
                                .session(session)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                )
                .andExpect(status().isOk()); // 로그아웃 성공

        mockMvc.perform(
                        post("/api/v1/members/logout")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                )
//                .andExpect(status().isOk()) // 로그아웃 성공
                .andExpect(status().isConflict()); // 로그아웃 중복

        // then

    }

    @Test
    public void 멤버_조회_페이징() throws Exception {
        // given
        int pageId = 1;
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SESSION_NAME_LOGIN, 1); // 회원 인증 인터셉터 통과위해
        // 테스트용 members 세팅
        List<FindMemberResponseDto> members = new ArrayList<>();
        FindMemberResponseDto m1 = new FindMemberResponseDto(111L, "테스트1", 1L);
        FindMemberResponseDto m2 = new FindMemberResponseDto(222L, "테스트2", 1L);
        FindMemberResponseDto m3 = new FindMemberResponseDto(333L, "테스트3", 1L);
        members.add(m1);
        members.add(m2);
        members.add(m3);

        // when
        when(memberService.findAllWithPage(1)).thenReturn(members);
        mockMvc.perform(
                        get("/api/v1/members/" + pageId)
                                .session(session)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                )
                .andExpect(status().isOk()) // 예상 응답
                .andDo(print());

        // then
        verify(memberService).findAllWithPage(1);
    }

}