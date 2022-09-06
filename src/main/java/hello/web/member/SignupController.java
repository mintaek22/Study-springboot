package hello.web.member;

import hello.domain.member.Member;
import hello.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class SignupController {

    private final MemberRepository memberRepository;

    @GetMapping("/add")
    public String signup(@ModelAttribute Member member){
        return "member/signup";
    }

    @PostMapping("/add")
    public String addMember(@Validated @ModelAttribute Member member, BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "member/signup";
        }

        Member save = memberRepository.save(member);
        log.info("회원등록되었습니다 LoginId={} password={}",save.getLoginId(),save.getPassword());
        return "redirect:/";
    }


}
