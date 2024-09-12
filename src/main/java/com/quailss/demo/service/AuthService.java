package com.quailss.demo.service;

import com.quailss.demo.domain.Member;
import com.quailss.demo.domain.dto.LoginDto;
import com.quailss.demo.domain.dto.RegisterDto;
import com.quailss.demo.repository.AuthRepository;
import jakarta.transaction.Transactional;
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
            if (email.equals(memberEmail.get().getEmail()) && passwordEncoder.matches(password,memberEmail.get().getPassword())) {
                LoginDto loginDto = LoginDto.toLoginDto(memberEmail.get());
                return loginDto;
            }
        }

        return null;
    }

    public String getAuthenticatedMemberId(String name, String phonenumber){
        Optional<Member> memberId = authRepository.findByPhonenumber(phonenumber);

        if(memberId.isPresent()){
            return memberId.get().getEmail();
        }

        return null;
    }


    public Long verifyAndResetPassword(String email, String phoneNumber, String password){
        Optional<Member> memberPhoneNumber = authRepository.findByPhonenumber(phoneNumber);

        if(memberPhoneNumber.isPresent()){
            Member member = memberPhoneNumber.get();
            String encryptedPassword = passwordEncoder.encode(password);
            member.setPassword(encryptedPassword);
            authRepository.save(member);
            return member.getId();
        }
        return null;
    }
}
