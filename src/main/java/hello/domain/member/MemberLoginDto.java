package hello.domain.member;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class MemberLoginDto {
    @NotEmpty
    private String loginId;

    @NotEmpty
    private String password;
}
