package com.quailss.demo.service;

import com.quailss.demo.domain.Member;
import com.quailss.demo.domain.dto.LoginDto;
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

        Member newMember = new Member(registerDto.getEmail(), encryptedPassword, registerDto.getName(), registerDto.getPhone(), registerDto.getBirth());

        Member registeredMember = authRepository.save(newMember);
    }

    public Optional<Member> findByEmail(String email){
        return authRepository.findByEmail(email);
    }

    public LoginDto verificationMember(String email, String password) {
        Optional<Member> memberEmail = authRepository.findByEmail(email);

        if (memberEmail.isPresent()) {
            if (email.equals(memberEmail.get().getEmail())) {
                memberEmail.map(m -> passwordEncoder.matches(password, m.getPassword())).orElse(null);
                LoginDto loginDto = LoginDto.toLoginDto(memberEmail.get());
                return loginDto;
            }
        }
        return null;
    }

    public String getAuthenticatedMemberId(String name, String phonenumber){
        Optional<Member> member = authRepository.findByNameAndPhonenumber(name, phonenumber);

        if(member.isPresent()){
            return member.get().getEmail();
        }

        return null;
    }
}
