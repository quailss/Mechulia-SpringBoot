package com.quailss.demo.service;

import com.quailss.demo.domain.Member;
import com.quailss.demo.domain.dto.RegisterDto;
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

        Member newMember = new Member(registerDto.getEmail(), encryptedPassword, registerDto.getName());

        Member registeredMember = authRepository.save(newMember);
    }

    public Optional<Member> findByEmail(String email){
        return authRepository.findByEmail(email);
    }

}
