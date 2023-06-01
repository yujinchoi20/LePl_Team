package team.lepl_team.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.lepl_team.Domain.Member.Member;
import team.lepl_team.Repository.MemberRepository;

import java.nio.channels.IllegalChannelGroupException;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    @Autowired
    MemberRepository memberRepository;

    @Transactional
    public Long join(Member member) {
        //중복 회원 검증
        validateDuplicateMember(member);

        //회원 저장
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findUid = memberRepository.findByUid(member.getUid());
        System.out.println("==============" + findUid);
        if(!findUid.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    public Member findOne(Long id) {
        return memberRepository.findOne(id);
    }

    public List<Member> findByUid(String uid) {
        return memberRepository.findByUid(uid);
    }
}
