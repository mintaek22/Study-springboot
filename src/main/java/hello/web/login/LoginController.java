package hello.web.login;

import hello.domain.member.MemberLoginDto;
import hello.service.LoginService;
import hello.domain.member.Member;
import hello.web.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String login(@ModelAttribute("member") MemberLoginDto form){
        return "member/login";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute("member") MemberLoginDto form,
                            @RequestParam(defaultValue = "/")String redirectURL,
                            BindingResult bindingResult,
                            HttpServletRequest request) throws SQLException {

        Member member = loginService.login_check(form.getLoginId(), form.getPassword());
        log.info("login? {}", member);
        //요청한 로그인 비밀번호 정보가 존재하지 않을 경우
        if (member == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "member/login";
        }

        log.info("정상적으로 로그인 되었습니다");

        //로그인 성공처리
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER,member);

        return "redirect:"+redirectURL;
    }

   @PostMapping("/logout")
    public String logout(HttpServletRequest request){

        //false는 세션이 없으면 null 반환
        HttpSession session = request.getSession(false);
        if(session != null){
            //세션이랑 내부 정보 삭제
            session.invalidate();
        }
        return "redirect:/";
    }

    private void expireCookie(HttpServletResponse response,String cookieName) {
        Cookie cookie = new Cookie(cookieName,null);
        //유효기간을 0 으로 설정
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
