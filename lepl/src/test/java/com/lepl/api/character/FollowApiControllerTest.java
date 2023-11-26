package com.lepl.api.character;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lepl.Service.character.CharacterService;
import com.lepl.Service.character.FollowService;
import com.lepl.Service.character.NotificationService;
import com.lepl.Service.member.MemberService;
import com.lepl.domain.character.Character;
import com.lepl.domain.character.Exp;
import com.lepl.domain.character.Follow;
import com.lepl.domain.member.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lepl.util.Messages.SESSION_NAME_LOGIN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = FollowApiController.class)
class FollowApiControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean // 가짜 객체
    FollowService followService;
    @MockBean
    MemberService memberService;
    @MockBean
    CharacterService characterService;
    @MockBean
    NotificationService notificationService;
    static MockHttpSession s; // 전역
    static final Long MEMBER_ID = 1L;
    static final Long MEMBER_ID2 = 2L;
    static final Long FOLLOW_ID = 1L;
    static final Long CHARACTER_ID = 1L;
    static final Long CHARACTER_ID2 = 2L;

    @BeforeEach
    public void 로그인_세션() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SESSION_NAME_LOGIN, MEMBER_ID); // 회원 인증 인터셉터 통과 + @Login memberId
        s = session;
    }

    /**
     * create, delete, findFollowing, findFollower
     */

    @Test
    public void 팔로우_생성() throws Exception {
        // given
        Map<String, Long> map = new HashMap<>();
        map.put("followingId", MEMBER_ID2);
        ObjectMapper obj = new ObjectMapper();
        String content = obj.writeValueAsString(map);

        Member member = Member.createMember("123456789", "팔로우 사용자");
        Character character = Character.createCharacter(Exp.createExp(0L, 0L, 1L), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        Follow follow = Follow.createFollow(Character.createCharacter(Exp.createExp(0L, 0L, 1L), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),1L);;
        Character findCharacter = Character.createCharacter(Exp.createExp(0L, 0L, 1L), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        // when
        when(memberService.findOne(MEMBER_ID)).thenReturn(member);
        when(characterService.findCharacterWithMember(MEMBER_ID)).thenReturn(character);
        when(characterService.findOne(MEMBER_ID2)).thenReturn(findCharacter);
        mockMvc.perform(
                        post("/api/v1/follow/new")
                                .session(s)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .content(content)
                )
                .andExpect(status().isCreated())
                .andDo(print());

        // then
        verify(memberService).findOne(MEMBER_ID);
        verify(characterService).findCharacterWithMember(MEMBER_ID);
        verify(characterService).findOne(MEMBER_ID2);
        verify(followService).join(any());
        verify(notificationService).join(any());
    }

    @Test
    public void 팔로우_삭제() throws Exception {
        // given
        Map<String, Long> map = new HashMap<>();
        map.put("followId", FOLLOW_ID);
        ObjectMapper obj = new ObjectMapper();
        String content = obj.writeValueAsString(map);
        Character character = Character.createCharacter(Exp.createExp(0L, 0L, 1L), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        character.setId(CHARACTER_ID);
        Character character2 = Character.createCharacter(Exp.createExp(0L, 0L, 1L), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        character2.setId(CHARACTER_ID2);
        Follow follow = Follow.createFollow(Character.createCharacter(Exp.createExp(0L, 0L, 1L), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),1L);
        follow.setId(FOLLOW_ID);
        follow.setCharacter(character);

        // when
        when(characterService.findCharacterWithMember(MEMBER_ID)).thenReturn(character);
        when(followService.findOne(FOLLOW_ID)).thenReturn(follow);
        mockMvc.perform(
                        post("/api/v1/follow/delete")
                                .session(s)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .content(content)
                )
                .andExpect(status().isOk())
                .andDo(print());
        when(characterService.findCharacterWithMember(MEMBER_ID)).thenReturn(character2);
        mockMvc.perform(
                        post("/api/v1/follow/delete")
                                .session(s)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .content(content)
                )
                .andExpect(status().isUnauthorized())
                .andDo(print());

        // then
        verify(followService).remove(any());
    }

    @Test
    public void 팔로잉_조회() throws Exception {
        // given
        Character character = Character.createCharacter(Exp.createExp(0L, 0L, 1L), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        character.setId(1L);
        List<Follow> follows = new ArrayList<>();
        List<Follow> follows2 = new ArrayList<>();
        Follow f1 = Follow.createFollow(Character.createCharacter(Exp.createExp(0L, 0L, 1L), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),1L);;
        Follow f2 = Follow.createFollow(Character.createCharacter(Exp.createExp(0L, 0L, 1L), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),1L);;
        Follow f3 = Follow.createFollow(Character.createCharacter(Exp.createExp(0L, 0L, 1L), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),1L);;
        follows.add(f1);
        follows.add(f2);
        follows.add(f3);

        // when
        when(characterService.findCharacterWithMember(MEMBER_ID)).thenReturn(character);
        when(followService.findAllWithFollowing(CHARACTER_ID)).thenReturn(follows);
        mockMvc.perform(
                get("/api/v1/follow/ing/all")
                        .session(s)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        )
                .andExpect(status().isOk())
                .andDo(print());
        when(followService.findAllWithFollowing(CHARACTER_ID)).thenReturn(follows2);
        mockMvc.perform(
                        get("/api/v1/follow/ing/all")
                                .session(s)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                )
                .andExpect(status().isNoContent())
                .andDo(print());

        // then
        verify(characterService, times(2)).findCharacterWithMember(MEMBER_ID);
        verify(followService, times(2)).findAllWithFollowing(CHARACTER_ID);
    }

    @Test
    public void 팔로워_조회() throws Exception {
        // given
        Character character = Character.createCharacter(Exp.createExp(0L, 0L, 1L), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        character.setId(1L);
        List<Follow> follows = new ArrayList<>();
        List<Follow> follows2 = new ArrayList<>();
        Follow f1 = Follow.createFollow(Character.createCharacter(Exp.createExp(0L, 0L, 1L), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),1L);;
        Follow f2 = Follow.createFollow(Character.createCharacter(Exp.createExp(0L, 0L, 1L), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),1L);;
        Follow f3 = Follow.createFollow(Character.createCharacter(Exp.createExp(0L, 0L, 1L), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),1L);;
        follows.add(f1);
        follows.add(f2);
        follows.add(f3);

        // when
        when(characterService.findCharacterWithMember(MEMBER_ID)).thenReturn(character);
        when(followService.findAllWithFollower(CHARACTER_ID)).thenReturn(follows);
        mockMvc.perform(
                        get("/api/v1/follow/er/all")
                                .session(s)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                )
                .andExpect(status().isOk())
                .andDo(print());
        when(followService.findAllWithFollower(CHARACTER_ID)).thenReturn(follows2);
        mockMvc.perform(
                        get("/api/v1/follow/er/all")
                                .session(s)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                )
                .andExpect(status().isNoContent())
                .andDo(print());

        // then
        verify(characterService, times(2)).findCharacterWithMember(MEMBER_ID);
        verify(followService, times(2)).findAllWithFollower(CHARACTER_ID);
    }

}