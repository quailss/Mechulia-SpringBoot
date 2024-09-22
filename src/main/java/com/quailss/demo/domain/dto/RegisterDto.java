package com.quailss.demo.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RegisterDto {
    @NotEmpty(message="이메일은 필수입니다.")
    @Email(message = "유효한 이메일 주소를 입력하세요.")
    private String email;

    @NotEmpty(message = "비밀번호는 필수입니다.")
    @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "비밀번호는 영문, 숫자, 특수문자 조합으로 8자 이상 입력해야 합니다."
    )
    private String password;

    @NotEmpty(message = "닉네임은 필수입니다.")
    private String name;

    @NotEmpty(message = "전화번호는 필수입니다.")
    @Pattern(
            regexp = "^\\d{10,11}$",
            message = "전화번호는 숫자 10자리 또는 11자리여야 합니다."
    )
    private String phone;

    @NotNull(message = "생년월일은 필수입니다.")
    private LocalDate birth;

}
