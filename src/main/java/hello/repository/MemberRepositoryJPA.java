package hello.repository;

import hello.domain.member.Member;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Transactional
public class MemberRepositoryJPA implements MemberRepository{

    private final EntityManager em;

    public MemberRepositoryJPA(EntityManager em) {
        this.em = em;
    }


    @Override
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    @Override
    public Member findByLoginId(String loginId) {

        String jpql ="select m from Member m where m.loginId= :loginId";
        return   em.createQuery(jpql,Member.class)
                .setParameter("loginId",loginId)
                .getSingleResult();
    }


}
