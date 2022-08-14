package hello.itemservice.web.member;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class MemberLoginForm {
    @NotEmpty
    private String loginId;

    @NotEmpty
    private String password;
}
