package com.quailss.demo.domain.dto;

import com.quailss.demo.domain.Bookmark;
import com.quailss.demo.domain.Review;
import com.quailss.demo.domain.enums.Provider;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RegisterDto {
    @NotEmpty(message="이메일은 필수입니다.")
    @Email(message = "유효한 이메일 주소를 입력하세요.")
    private String email;

    @NotEmpty(message = "비밀번호는 필수입니다.")
    private String password;    //필요 시 나중에 패턴 추가

    @NotEmpty(message = "닉네임은 필수입니다.")
    private String name;

    @Nullable
    private Provider provider;

    @Nullable
    private String provider_id;

    @Nullable
    private List<Bookmark> bookmarkList = new ArrayList<>();
    @Nullable
    private List<Review> reviewList = new ArrayList<>();

}
