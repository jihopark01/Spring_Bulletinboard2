package com.sparta.board.dto;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Optional;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SignUpRequestDto {

    @NotBlank(message = "username은 필수 입력값입니다.")
    @Pattern(regexp = "^[a-z0-9]{4,8}$", message = "username은 알파벳소문자,숫자로 구성된 4자 이상,10이하여야 합니다.")
    private String username;


    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Pattern(regexp = "^[A-Za-z\\d$@$!%*?&]{8,15}$", message = "비밀번호는 알파벳대소문자,숫자, 특수문자로 구성된 8자 이상,15이하여야 합니다.")
    private String password;

    private String admin ;
    private String adminToken = "";
}
