package com.quailss.demo.domain.dto;

import com.quailss.demo.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class LoginDto {

    private String email;

    private String password;

    public static com.quailss.demo.domain.dto.LoginDto toLoginDto(Member member) {
        com.quailss.demo.domain.dto.LoginDto loginDto = new com.quailss.demo.domain.dto.LoginDto();
        loginDto.setEmail(member.getEmail());
        loginDto.setPassword(member.getPassword());

        return loginDto;
    }
}
