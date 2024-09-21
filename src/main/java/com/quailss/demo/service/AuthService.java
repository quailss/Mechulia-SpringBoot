package com.quailss.demo.service;

import com.quailss.demo.domain.Member;
import com.quailss.demo.domain.dto.LoginDto;
import com.quailss.demo.domain.dto.RegisterDto;
import com.quailss.demo.domain.enums.Provider;
import com.quailss.demo.repository.AuthRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterDto registerDto){
        String encryptedPassword = passwordEncoder.encode(registerDto.getPassword());

        Member newMember = new Member(registerDto.getEmail(), encryptedPassword, registerDto.getName(), registerDto.getPhone(),registerDto.getBirth());

        Member registeredMember = authRepository.save(newMember);
    }

    public LoginDto verificationMember(String email, String password) {
        Optional<Member> memberEmail = authRepository.findByEmailAndProvider(email, Provider.LOCAL);

        if (memberEmail.isPresent()) {
            if (email.equals(memberEmail.get().getEmail()) && passwordEncoder.matches(password,memberEmail.get().getPassword())) {
                LoginDto loginDto = LoginDto.toLoginDto(memberEmail.get());
                return loginDto;
            }
        }
        return null;
    }

    public String getAuthenticatedMemberId(String name, String phonenumber){
        Optional<Member> member = authRepository.findByPhonenumber(phonenumber);

        if(member.isPresent()){
            return member.get().getEmail();
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

    public Optional<Member> findByEmailAndProvider(String loggedInEmail, Provider provider) {
        return authRepository.findByEmailAndProvider(loggedInEmail, provider);
    }

    public boolean isEmailAvailable(String email) {
        Optional<Member> memberOptional = findByEmailAndProvider(email, Provider.LOCAL);
        return memberOptional.isEmpty();
    }

    public Map<String, Object> getMemberInfoFromSession(HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        String loggedInEmail  = (String)session.getAttribute("Email");
        Provider provider = (Provider) session.getAttribute("Provider");

        if(loggedInEmail == null) {
            response.put("loggedIn", false);
            response.put("nickname", "");
            response.put("email", "");
        } else {
            Optional<Member> memberOptional = findByEmailAndProvider(loggedInEmail, provider);

            if(memberOptional.isEmpty()){
                response.put("loggedIn", false);
                response.put("nickname", "");
                response.put("email", "");
            } else {
                response.put("loggedIn", true);
                response.put("nickname", memberOptional.get().getName());
                response.put("email", memberOptional.get().getEmail());
            }
        }

        return response;
    }

    public Optional<Member> getLoggedInMember(HttpSession session) {
        String loggedInEmail = (String) session.getAttribute("Email");
        Provider provider = (Provider) session.getAttribute("Provider");

        if(loggedInEmail == null) {
            return Optional.empty();
        }

        return findByEmailAndProvider(loggedInEmail, provider);
    }
}
