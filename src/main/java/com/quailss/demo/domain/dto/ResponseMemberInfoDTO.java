package com.quailss.demo.domain.dto;

import jakarta.validation.constraints.NotEmpty;
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
