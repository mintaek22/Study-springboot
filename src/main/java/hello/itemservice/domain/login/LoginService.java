package hello.itemservice.domain.login;

import hello.itemservice.domain.member.Member;
import hello.itemservice.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    private final MemberRepository memberRepository;

    /**
     * @return null이면 로그인 실패
     */
     public Member login_check(String loginId, String password) throws SQLException {

         Member loginMember = memberRepository.findByLoginId(loginId);

         if(Objects.equals(loginMember.getPassword(), password)){
             return loginMember;
         }
         else{
             return null;
         }
     }
}
