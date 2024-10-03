package com.quailss.demo.domain.dto;

import com.quailss.demo.domain.enums.Provider;
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

    private Provider provider;

    public static ResponseMemberInfoDTO getMemberinfo(String email, String name, String phoneNumber, LocalDate birthday, Provider provider){
        ResponseMemberInfoDTO responseMemberInfoDTO = new ResponseMemberInfoDTO();
        responseMemberInfoDTO.setEmail(email);
        responseMemberInfoDTO.setName(name);
        responseMemberInfoDTO.setPhoneNumber(phoneNumber);
        responseMemberInfoDTO.setBirthday(birthday);
        responseMemberInfoDTO.setProvider(provider);

        return responseMemberInfoDTO;
    }

}
