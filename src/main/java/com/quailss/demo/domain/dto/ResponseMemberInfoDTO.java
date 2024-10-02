package com.quailss.demo.domain.dto;

import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
public class ResponseMemberInfoDTO {

    private String email;

    private String name;

    private String phoneNumber;

    private LocalDate birthday;

    @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "비밀번호는 영문, 숫자, 특수문자 조합으로 8자 이상 입력해야 합니다."
    )
    private String password;

    public static ResponseMemberInfoDTO getMemberinfo(String email, String name, String phoneNumber, LocalDate birthday){
        ResponseMemberInfoDTO responseMemberInfoDTO = new ResponseMemberInfoDTO();
        responseMemberInfoDTO.setEmail(email);
        responseMemberInfoDTO.setName(name);
        responseMemberInfoDTO.setPhoneNumber(phoneNumber);
        responseMemberInfoDTO.setBirthday(birthday);

        return responseMemberInfoDTO;
    }

}
