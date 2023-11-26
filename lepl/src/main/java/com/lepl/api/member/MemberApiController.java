package com.lepl.api.member;


import com.lepl.Service.character.CharacterService;
import com.lepl.Service.character.ExpService;
import com.lepl.Service.member.MemberService;
import com.lepl.api.argumentresolver.Login;
import com.lepl.api.member.dto.FindMemberResponseDto;
import com.lepl.domain.character.Character;
import com.lepl.domain.character.Exp;
import com.lepl.domain.member.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.lepl.util.Messages.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/members")
public class MemberApiController {
    private final MemberService memberService;
    private final ExpService expService;
    private final CharacterService characterService;

    /**
     * login, loginTest(사용X), register, logout, logoutTest(사용X), findAllWithPage, testUidV1(사용X), testUidV2(사용X)
     */

    /**
     * 로그인
     * 입력 -> JSON
     * UID로 조회 후 세션Id 응답 쿠키
     */
    @PostMapping("/login") // 입력 => json 이용
    public ResponseEntity<String> login(@RequestBody @Valid LoginMemberRequestDto loginDto, HttpServletRequest request) {
        Member findMember = memberService.findByUid(loginDto.getUid());
        // 회원아닌 경우
        if (findMember == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(FAIL_LOGIN); // 404 : Not Found
        }
        // 회원인 경우 : 로그인 성공 처리  => 세션Id 응답 쿠키
        // 세션 있으면 세션 반환, 없으면 신규 세션 생성
        HttpSession session = request.getSession(); // UUID 형태로 알아서 생성 (기본값 : true)
        // 세션에 로그인 회원 정보 보관
        session.setAttribute(SESSION_NAME_LOGIN, findMember.getId());

        return ResponseEntity.status(HttpStatus.OK).body(SUCCESS_LOGIN); // 200 : OK, 쿠키에 세션을 담아서 같이 전송하므로 클라는 인증서를 발급받은 효과
    }

    // test용 GET (웹에서 쿠키 확인)
    @GetMapping("/login/{uid}")
    public String loginTest(@PathVariable("uid") String uid, HttpServletRequest request) {
        Member loginMember = memberService.findByUid(uid);
        if (loginMember == null) { // 회원 아닌경우
            return "회원이 아닙니다."; // 에러 코드 날려주던지 등등
        }
        HttpSession session = request.getSession();
        session.setAttribute(SESSION_NAME_LOGIN, loginMember.getId());
        return "회원 인증 완료"; // 쿠키에 세션을 담아서 같이 전송하므로 클라는 인증서를 발급받은 효과
    }

    /**
     * 회원가입
     * 입력 -> JSON
     * UID, Nickname 필수
     */
    @PostMapping("/register")
    public ResponseEntity<RegisterMemberResponseDto> register(@RequestBody @Valid RegisterMemberRequestDto registerDto) {
        Member member = Member.createMember(registerDto.uid, registerDto.nickname);

        Exp exp = Exp.createExp(0L, 0L, 1L);
        expService.join(exp);
        Character character = Character.createCharacter(exp, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        characterService.join(character);

        // 중복회원 처리
        memberService.validateDuplicateMember(member);

        member.setCharacter(character);
        member = memberService.join(member);

        return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterMemberResponseDto(member));
    }

    /**
     * 로그아웃
     * 입력 -> 쿠키에 세션정보
     * 쿠키에 세션정보를 통해 메모리할당 제거
     */
    @PostMapping("/logout") // 입력 => 쿠키의 세션정보 이용
    public ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 세션 없으면 null을 반환
        if (session == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(VALID_LOGOUT);
        }
        session.invalidate(); // 세션 제거
        return ResponseEntity.status(HttpStatus.OK).body(LOGOUT);
    }

    // test용 GET (웹에서 쿠키 확인)
    @GetMapping("/logout/{uid}")
    public String logoutTest(@PathVariable("uid") String uid, HttpServletRequest request) {
        Member loginMember = memberService.findByUid(uid);
        if (loginMember == null) { // 회원 아닌경우
            return "회원이 아닙니다."; // 에러 코드 날려주던지 등등
        }// 그냥 회원체크 테스트
        HttpSession session = request.getSession(false); // 세션 없으면 null을 반환
        if (session != null) {
            session.invalidate(); // 세션 제거
        }
        return "로그아웃 성공";
    }

    /**
     * 멤버 조회 -> 페이징 사용(size:10)
     * pageId 필수
     * FindMemberResponseDto 에 내용 추가하고싶으면 수정
     */
    @GetMapping("/{pageId}")
    public ResponseEntity<List<FindMemberResponseDto>> findAllWithPage(@PathVariable int pageId) {
        List<FindMemberResponseDto> result = memberService.findAllWithPage(pageId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


    // test용 GET (웹에서 쿠키 확인) => "인터셉터" 동작도 확인 => Uid 얻어내나 확인
    @GetMapping("/v1/testUid")
    public String testUidV1(HttpServletRequest request) {
        HttpSession session = request.getSession();
//        Member member = (Member) session.getAttribute("login_member");
        Long memberId = Long.valueOf(session.getAttribute(SESSION_NAME_LOGIN).toString());
        Member member = memberService.findOne(memberId);
        return "테스트 uid : " + member.getUid();
    }

    // @Login Long 으로 바로 멤버 id 가져와서 멤버 객체 조회 되는지 테스트 => 이제부터 위 방법 말고 이 방법을 사용하면 된다.
    @GetMapping("/v2/testUid")
    public String testUidV2(@Login Long id) {
        Member member = memberService.findOne(id);
        return "테스트 uid : " + member.getUid();
    }

    @Getter
    static class RegisterMemberResponseDto {
        private Long id;
        private String uid;
        private String nickname;

        public RegisterMemberResponseDto(Member member) {
            this.id = member.getId();
            this.uid = member.getUid();
            this.nickname = member.getNickname();
        }
    }

    @Getter
    static class RegisterMemberRequestDto {
        @NotEmpty(message = "UID는 필수입니다.") // 에러코드 날려줘도 됨 (Valid와 세트)
        private String uid;
        @NotEmpty(message = "닉네임은 필수입니다.") // 에러코드 날려줘도 됨 (Valid와 세트)
        private String nickname;
    }

    @Getter
    static class LoginMemberRequestDto {
        @NotEmpty(message = "UID는 필수입니다.")
        private String uid;
    }
}