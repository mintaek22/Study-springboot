package hello.itemservice.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
public class MemberRepository {

    private static Map<Long, Member> memberMap = new ConcurrentHashMap<>();

    private static long sequence = 0L;

    public Member save(Member member) {
        member.setId(++sequence);
        memberMap.put(member.getId(),member);
        return member;
    }

    public Member findById(long id) {
        return memberMap.get(id);
    }

    public Optional<Member> findByLoginId(String loginId) {
        return findAll().stream()
                .filter(m -> m.getLoginId().equals(loginId))
                .findFirst();
    }

    public List<Member> findAll() {
        return new ArrayList<>(memberMap.values());
    }

    public void update(Long itemId, Member updateParam) {
        Member findMember = findById(itemId);
        findMember.setLoginId(updateParam.getLoginId());
        findMember.setUsername(updateParam.getUsername());
        findMember.setPassword(updateParam.getPassword());
    }

    public void clearStore() {
        memberMap.clear();
    }
}
