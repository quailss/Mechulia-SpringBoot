package com.quailss.demo.service;

import com.quailss.demo.domain.Member;
import com.quailss.demo.domain.enums.Provider;
import com.quailss.demo.repository.AuthRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
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
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // OAuth2UserRequest를 사용하여 사용자 정보를 로드
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 클라이언트 이름 (카카오, 네이버 등)을 얻어옴
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // kakao, naver 등
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        // 사용자 정보를 맵 형태로 추출
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 사용자 정보를 기반으로 CustomOAuth2User를 생성
        OAuth2User customUser = processOAuth2User(registrationId, userNameAttributeName, attributes);

        return customUser;
    }

    private OAuth2User processOAuth2User(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        String email;
        String name;
        Provider provider;
        String provider_id;

        if ("naver".equals(registrationId)) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            email = (String) response.get("email");
            name = (String) response.get("nickname");
            provider_id = (String) response.get("id");
            provider = Provider.NAVER;
        } else if ("kakao".equals(registrationId)) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            email = (String) kakaoAccount.get("email");
            name = (String) profile.get("nickname");
            provider_id = attributes.get("id").toString();
            provider = Provider.KAKAO;
        } else {
            throw new OAuth2AuthenticationException("Unsupported provider: " + registrationId);
        }
        // 소셜 로그인 사용자가 이미 등록되어 있는지 확인
        Optional<Member> memberOptional = authRepository.findByProviderAndProviderId(provider, provider_id);

        Member member;
        if (memberOptional.isPresent()) {
            member = memberOptional.get();
        } else {
            // 회원 정보가 없으면 신규 등록
            member = new Member(email, name, provider, provider_id);
            authRepository.save(member);
        }

        httpSession.setAttribute("email", member.getEmail());
        // 필요한 경우 사용자 정보를 업데이트
        // 예: member.updateProfile(name);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                userNameAttributeName);
    }
}