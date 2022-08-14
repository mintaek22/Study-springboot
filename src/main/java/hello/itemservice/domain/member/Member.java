package hello.itemservice.domain.member;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class Member {

    //primary key
    private Long id;

    @NotEmpty
    private String loginId;

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;
}
