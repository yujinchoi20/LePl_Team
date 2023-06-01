package team.lepl_team.Service;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import team.lepl_team.Domain.Member.Member;
import team.lepl_team.Repository.MemberRepository;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberService memberService;

    @Test
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("choi");
        member.setUid("12341234");

        //when
        Long findId = memberService.join(member);

        //then
        Assertions.assertThat(member).isEqualTo(memberRepository.findOne(findId));
    }

    @Test
    public void 중복체크() throws Exception {
        //given
        Member member1 = new Member();
        member1.setUid("1234");

        Member member2 = new Member();
        member2.setUid("1234");

        //when
        memberService.join(member1);

        //then
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> memberService.join(member2));
        Assertions.assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
    }

    @Test
    public void 회원조회() throws Exception {
        //given
        Member member1 = new Member();
        member1.setUid("1234");
        member1.setName("choi");

        memberService.join(member1);

        //when
        Member findMember = memberService.findOne(member1.getId());

        //then
        Assertions.assertThat(member1).isEqualTo(findMember);
    }

    @Test
    public void 회원_식별자() throws Exception {
        //given
        Member member = new Member();
        member.setName("choi");
        member.setUid("1234");

        memberService.join(member);

        //when
        List<Member> findUID = memberService.findByUid(member.getUid());

        //then
        for(Member m : findUID) {
            System.out.println("Name : " + m.getName());
        }

    }
}