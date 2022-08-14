package hello.itemservice.web.login;

import hello.itemservice.domain.login.LoginService;
import hello.itemservice.domain.member.Member;
import hello.itemservice.web.member.MemberLoginForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    @GetMapping
    public String login(@ModelAttribute("member") MemberLoginForm form){
        return "member/login";
    }

    @PostMapping
    public String addMember(@Validated @ModelAttribute("member") MemberLoginForm form,
                            BindingResult bindingResult,
                            HttpServletResponse response){

        Member member = loginService.login_check(form.getLoginId(), form.getPassword());
        log.info("login? {}", member);
        if (member == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "member/login";
        }

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "member/signup";
        }

        log.info("정상적으로 로그인 되었습니다");

        //쿠키
        //따로 시간정보를 주지 않으면 세션쿠키(브라우저 종료시 사라짐)
        Cookie idCookie = new Cookie("memberId", String.valueOf(member.getId()));
        response.addCookie(idCookie);
        return "redirect:/";
    }
}
