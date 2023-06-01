package team.lepl_team.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import team.lepl_team.Domain.Member.Member;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Member member) { //회원 저장
        em.persist(member);
    }

    public Member findOne(Long memberId) { //회원 한 명 조회
        return em.find(Member.class, memberId);
    }

    public List<Member> findAll() { //회원 전체 조회 -> 친구추가 기능에 사용(?)
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByUid(String uid) {
         return em.createQuery("select m from Member m where m.uid = :uid", Member.class)
                .setParameter("uid", uid)
                .getResultList();
    }
}
