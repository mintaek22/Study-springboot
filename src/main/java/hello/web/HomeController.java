package hello.web;

import hello.domain.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {


    //required는 필수여부 로그인 안한 사람도 들어와야되므로 required = false 유지
    //어노테이션 이용
    @GetMapping
    public String home(@SessionAttribute(name=SessionConst.LOGIN_MEMBER,required = false) Member loginMember,
                       Model model){

        //세션에 회원 데이터가 없으면 home
        if (loginMember == null) {
            return "home";
        }

        model.addAttribute("member",loginMember);
        return "loginHome";
    }
}
