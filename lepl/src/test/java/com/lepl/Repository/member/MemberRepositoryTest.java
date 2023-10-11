package com.lepl.Repository.member;


import com.lepl.Repository.character.CharacterRepository;
import com.lepl.Service.member.MemberService;
import com.lepl.domain.character.Character;
import com.lepl.domain.member.Member;
import com.lepl.domain.member.Profile;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

// 현재 메모리에서 테스트하기 때문에 h2 DB에 적용을 보려면 main 함수에서!!
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
@Slf4j
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired EntityManager em;

    @Test
    @Transactional
    @Rollback(false)
    public void 회원_로그인() throws Exception {
        // given
        Member member = new Member();
        member.setUid("123");
        member.setNickname("test1");

        Character character = new Character();
        Profile profile = new Profile();
        member.setCharacter(character);
        member.setProfile(profile);

        // when
        memberRepository.save(member);

        Member findMember1 = memberRepository.findOne(member.getId());
        Member findMember2 = memberRepository.findByUid("123");

        // then
        Assertions.assertEquals(member, findMember1);
        Assertions.assertEquals(member, findMember2);
    }
}