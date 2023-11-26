package com.lepl.api.character;

import com.lepl.Service.character.CharacterService;
import com.lepl.Service.character.NotificationService;
import com.lepl.domain.character.Character;
import com.lepl.domain.character.Exp;
import com.lepl.domain.character.Notification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static com.lepl.util.Messages.SESSION_NAME_LOGIN;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = NotificationApiController.class) // 컨트롤러 테스트
class NotificationApiControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean // 가짜 객체
    NotificationService notificationService;
    @MockBean
    CharacterService characterService;
    static final Long MEMBER_ID = 1L;
    static final Long CHARACTER_ID = 1L;

    /**
     * findAllWithCharacter
     */
    @Test
    public void 캐릭터의_알림조회() throws Exception {
        // given
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SESSION_NAME_LOGIN, MEMBER_ID); // 회원 인증 인터셉터 통과 + @Login memberId
        Exp exp = Exp.createExp(0L, 0L, 1L);
        Character character = Character.createCharacter(exp, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        character.setId(CHARACTER_ID);

        List<Notification> notifications = new ArrayList<>();
        List<Notification> notifications2 = new ArrayList<>();
        Notification no1 = Notification.createNotification(character, "알림테스트1");
        Notification no2 = Notification.createNotification(character, "알림테스트2");
        Notification no3 = Notification.createNotification(character, "알림테스트3");
        notifications.add(no1);
        notifications.add(no2);
        notifications.add(no3);

        // when
        when(characterService.findCharacterWithMember(MEMBER_ID)).thenReturn(character);
        when(notificationService.findAllWithCharacter(CHARACTER_ID)).thenReturn(notifications);
        mockMvc.perform(
                        get("/api/v1/notification/all")
                                .session(session)
                                .characterEncoding("UTF-8")
                )
                .andExpect(status().isOk()) // 200 TEST
                .andExpect(jsonPath("$[0].content").value("알림테스트1")) // 응답 body 의 json 확인
                .andExpect(jsonPath("$[2].content").exists())
                .andExpect(jsonPath("$[3]").doesNotExist()) // 없어야 정상
                .andDo(print());

        when(notificationService.findAllWithCharacter(CHARACTER_ID)).thenReturn(notifications2);
        mockMvc.perform(
                        get("/api/v1/notification/all")
                                .session(session)
                                .characterEncoding("UTF-8")
                )
                .andExpect(status().isNoContent()) // 204 TEST
                .andExpect(jsonPath("$[0]").doesNotExist()) // null
                .andDo(print());

        // then
        verify(characterService, times(2)).findCharacterWithMember(MEMBER_ID);
        verify(notificationService, times(2)).findAllWithCharacter(CHARACTER_ID);
    }
}