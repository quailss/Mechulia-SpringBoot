package com.quailss.demo.service;

import com.quailss.demo.domain.Member;
import com.quailss.demo.domain.enums.Provider;
import com.quailss.demo.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final AuthRepository authRepository;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        //루피님
        return null;
    }

    private Member registerNewUser(Map<String, Object> attributes, String providerId) {
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("nickname");
        //String profileImage = (String) attributes.get("profile_image");

        Member newMember = Member.builder()
                .email(email)
                .name(name)
                .provider(Provider.KAKAO)
                .provider_id(providerId)
        //        .profile_image(profileImage)
                .build();

        return authRepository.save(newMember);
    }
}