package team.lepl_team.Repository;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import team.lepl_team.Domain.Member.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception{
        //given
        Member member = new Member();
        member.setName("choi");
        member.setUid("12345abcde");

        //when
        memberRepository.save(member);
        Member findMember = memberRepository.findOne(member.getId());

        //then
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void 회원조회() throws Exception {
        //given
        Member member = new Member();
        member.setName("choi");
        member.setUid("1234");

        memberRepository.save(member);

        //when
        Member findMember = memberRepository.findOne(member.getId());

        //then
        System.out.println("UID: " + findMember.getUid());
    }

    @Test
    public void 전체_회원조회() throws Exception {
        //given
        Member member = new Member();
        member.setName("choi");
        member.setUid("1234");

        Member member2 = new Member();
        member2.setName("yujin");
        member2.setUid("12345");

        memberRepository.save(member);
        memberRepository.save(member2);

        //when
        List<Member> members = memberRepository.findAll();

        //then
        System.out.println("NUM : " + members.size());
    }

    @Test
    public void 회원_식별자() throws Exception {
        //given
        Member member = new Member();
        member.setName("choi");
        member.setUid("1234");

        Member member2 = new Member();
        member2.setName("yujin");
        member2.setUid("12345");

        memberRepository.save(member);
        memberRepository.save(member2);


        //when
        List<Member> findUID = memberRepository.findByUid(member.getUid());
        List<Member> findUID2 = memberRepository.findByUid(member2.getUid());

        //then
        for(Member m : findUID) {
            System.out.println("NAME1 : " + m.getName());
        }

        for(Member m : findUID2) {
            System.out.println("NAME2 : " + m.getName());
        }
    }
}