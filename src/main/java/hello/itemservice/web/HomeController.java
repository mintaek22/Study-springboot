package hello.itemservice.web;

import hello.itemservice.domain.member.Member;
import hello.itemservice.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;

    //required는 필수여부 로그인 안한 사람도 들어와야되므로 required = false 유지
    @GetMapping
    public String home(@CookieValue(name="memberId",required = false) Long memberId, Model model){
        //로그인 상태x
        if(memberId == null){
            return "home";
        }

        //쿠키에 정보는 있지만 저장소에 정보가 없는 경우
        Member member = memberRepository.findById(memberId);
        if(member == null){
            return "home";
        }

        model.addAttribute("member",member);
        return "loginHome";
    }
}
