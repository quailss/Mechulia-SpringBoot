package com.quailss.demo.service;

import com.quailss.demo.domain.Member;
import com.quailss.demo.domain.enums.Provider;
import com.quailss.demo.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final AuthRepository authRepository;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        //루피님
        return null;
    }
    //카카오
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
    //naver
    private OAuth2User processOAuth2User(Provider registrationId, Map<String, Object> attributes) {
        String providerId = (String) attributes.get("id");
        String email = (String) attributes.get("email");

        Optional<Member> memberOptional = authRepository.findByProviderAndProviderId(registrationId, providerId);

        if (memberOptional.isPresent()) {
            // 사용자가 이미 존재하는 경우 (로그인)
            return new DefaultOAuth2User(
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                    attributes,
                    "name");
        } else {
            // 사용자가 존재하지 않는 경우 (회원가입)
            Member newMember = Member.builder()
                    .email(email)
                    .name(providerId)
                    .provider(Provider.NAVER)
                    .provider_id(providerId)
                    .build();

            authRepository.save(newMember);

            return new DefaultOAuth2User(
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                    attributes,
                    "name");
        }
    }
}