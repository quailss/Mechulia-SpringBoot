package com.quailss.demo.service;

import com.quailss.demo.domain.Member;
import com.quailss.demo.domain.dto.LoginDto;
import com.quailss.demo.domain.dto.RegisterCommand;
import com.quailss.demo.domain.enums.MemberStatus;
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

    public void register(RegisterCommand registerCommand){
        String encryptedPassword = passwordEncoder.encode(registerCommand.getPassword());

        Member newMember = new Member(registerCommand.getEmail(), encryptedPassword, registerCommand.getName(), registerCommand.getPhone(), registerCommand.getBirth());

        Member registeredMember = authRepository.save(newMember);
    }

    public LoginDto verificationMember(String email, String password) {
        Optional<Member> memberEmail = authRepository.findByEmailAndProvider(email, Provider.LOCAL);

        if (memberEmail.isPresent()) {
            checkingOfWithdrawnMembers(memberEmail.get());
            if (email.equals(memberEmail.get().getEmail()) && passwordEncoder.matches(password,memberEmail.get().getPassword())) {
                LoginDto loginDto = LoginDto.toLoginDto(memberEmail.get());
                return loginDto;
            }
        }

        throw new NullPointerException();
    }

    private void checkingOfWithdrawnMembers(Member member){

        if(member.getStatus().equals(MemberStatus.DEACTIVATED)){
            member.setStatus(null);
            authRepository.save(member);
        }

    }

    public String getAuthenticatedMemberId(String name, String phonenumber){
        Optional<Member> member = authRepository.findByNameAndPhonenumber(name, phonenumber);

        if(member.isPresent()){
            return member.get().getEmail();
        }

        throw new NullPointerException();
    }

    public Long verifyAndResetPassword(String email, String phoneNumber, String newPassword){
        Optional<Member> memberPhoneNumber = authRepository.findByEmailAndPhonenumber(email,phoneNumber);

        if(memberPhoneNumber.isPresent()){
            Member member = memberPhoneNumber.get();
            String encryptedPassword = passwordEncoder.encode(newPassword);
            member.setPassword(encryptedPassword);
            authRepository.save(member);
            return member.getId();
        }
        throw new NullPointerException();
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
