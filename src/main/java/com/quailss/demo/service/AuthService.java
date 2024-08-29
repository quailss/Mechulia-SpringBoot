package com.quailss.demo.service;

import com.quailss.demo.domain.Member;
import com.quailss.demo.domain.dto.RegisterDto;
import com.quailss.demo.domain.enums.Provider;
import com.quailss.demo.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterDto registerDto){
        String encryptedPassword = passwordEncoder.encode(registerDto.getPassword());

        Member member = Member.builder()
                .email(registerDto.getEmail())
                .password(encryptedPassword)
                .name(registerDto.getName())
                .provider(null)
                .provider_id(null)
                .build();

        Member registeredMember = authRepository.save(member);
    }

    public Optional<Member> findByEmail(String email){
        return authRepository.findByEmail(email);
    }

    public Optional<Member> findByProviderAndProviderId(Provider provider, String providerId) {
        return authRepository.findByProviderAndProviderId(provider, providerId);
    }
}
