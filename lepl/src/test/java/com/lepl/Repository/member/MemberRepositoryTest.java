package com.lepl.Repository.member;


import com.lepl.domain.member.Member;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

// 현재 메모리에서 테스트하기 때문에 h2 DB에 적용을 보려면 main 함수에서!!
@SpringBootTest
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired EntityManager em;

    @Test
    @Transactional
    @Rollback(false)
    public void testMember() throws Exception {
        // given
        Member member = new Member();
        member.setUid("123");
        member.setNickname("test1");
        // when
        memberRepository.save(member);
//        em.flush(); // 강제 flush

        Member findMember1 = memberRepository.findOne(member.getId());
        Member findMember2 = memberRepository.findByUid("123");
//        Member findMember2 = memberRepository.findByUid("1234");
        System.out.println(findMember2);

        // then
        Assertions.assertEquals(member, findMember1);
        Assertions.assertEquals(member, findMember2);
        System.out.println(member.getUid());
        System.out.println(findMember1.getUid());
        System.out.println(findMember2.getId());
    }
}