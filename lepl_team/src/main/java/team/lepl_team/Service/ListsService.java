package team.lepl_team.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.lepl_team.Domain.List.Lists;
import team.lepl_team.Repository.ListsRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ListsService {
    //날짜 중복 잡아야 함.

    @Autowired ListsRepository listsRepository;

    @Transactional
    public Long enroll(Lists lists) {
        //중복 일정(날짜) 검증
        validateDuplicateLists(lists); //추가하려는 일정이 이미 존재하는지 확인.

        //일정 저장
        listsRepository.save(lists);
        return lists.getId();
    }

    //중복 일정 검증
    private void validateDuplicateLists(Lists lists) {
        List<Lists> date = listsRepository.findByDate(lists.getDate());

        if(!date.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 일정(날짜)입니다.");
        }
    }

    public Lists findOne(Long id) {
        return listsRepository.findOne(id);
    }

    public List<Lists> findAll() {
        return listsRepository.findAll();
    }
}
