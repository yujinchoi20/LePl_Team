package com.lepl.Service.member;

import com.lepl.Repository.member.MemberRepository;
import com.lepl.domain.member.Member;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional // 서비스 부분은 대부분 트랜잭션 사용
//@Rollback(false)
public class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {
        // given
        Member member = new Member();
        member.setUid("12345");
        member.setNickname("test5");
        // when
        Long saveId = memberService.join(member);

        // then
        // DB에 저장된 member를 찾으려고 레퍼지토리의 함수 사용
        Assertions.assertEquals(member, memberRepository.findOne(saveId));
    }

    // 일부러 예외가 터지게끔 코드를 실행해서 예외가 발생하는지 보는 테스트
    @Test() // 해당 예외 터지면 종료해줌
//    @Test
    public void 중복_회원_예외() throws Exception {
        // given
        Member member1 = new Member();
        member1.setUid("123");
        member1.setNickname("test1");
        Member member2 = new Member();
        member2.setUid("123"); // 중복
        member2.setNickname("test2");

        // when
        memberService.join(member1);
        memberService.join(member2); // 예외가 발생해야 함. (예외 터지게끔 보낸상황)

        // then
        Assertions.fail("예외가 발생해야 한다."); // 위에서 문제가 없으면 여기까지 온다.
    }
}