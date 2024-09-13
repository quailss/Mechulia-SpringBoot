package com.quailss.demo.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SetPasswordDto {

    private String email;

    private String phoneNumber;

    private String password;
}
