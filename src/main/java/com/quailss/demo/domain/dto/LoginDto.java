package com.quailss.demo.domain.dto;

import com.quailss.demo.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginDto {

    private String email;

    private String password;

    public static LoginDto toLoginDto(Member member) {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail(member.getEmail());
        loginDto.setPassword(member.getPassword());

        return loginDto;
    }
}
