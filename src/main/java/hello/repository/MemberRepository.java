package hello.repository;

import hello.domain.member.Member;

public interface MemberRepository {

    public Member save(Member member);

    public Member findByLoginId(String loginId);

}
