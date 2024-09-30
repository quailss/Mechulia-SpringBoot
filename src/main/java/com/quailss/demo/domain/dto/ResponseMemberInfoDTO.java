package com.quailss.demo.domain.dto;

import lombok.*;

import java.time.LocalDate;



@Getter
@Setter
public class ResponseMemberInfoDTO {


    private String name;

    private String phoneNumber;

    private LocalDate birthday;

    public static ResponseMemberInfoDTO getMemberinfo(String name, String phoneNumber, LocalDate birthday){
        ResponseMemberInfoDTO responseMemberInfoDTO = new ResponseMemberInfoDTO();
        responseMemberInfoDTO.setName(name);
        responseMemberInfoDTO.setPhoneNumber(phoneNumber);
        responseMemberInfoDTO.setBirthday(birthday);

        return responseMemberInfoDTO;
    }
}
