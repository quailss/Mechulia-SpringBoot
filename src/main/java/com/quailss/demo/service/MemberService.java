package com.quailss.demo.service;

import com.quailss.demo.domain.Member;
import com.quailss.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<Member> findById(Long member_id){
        return memberRepository.findById(member_id);
    }

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }


    // 휴대폰 번호 변경
    public String changePhoneNumber(String memberSession, String phoneNumber){
        Optional<Member> memberPhoneNumber = findByEmail(memberSession);
        if(memberPhoneNumber.isPresent()){
            Member member = memberPhoneNumber.get();
            member.setPhonenumber(phoneNumber);
            memberRepository.save(member);
            return member.getPhonenumber();
        }

        return null;
    }

    // 이름 변경
    public Long changeName(String memberSession, String name){
        Optional<Member> memberName = memberRepository.findByEmail(memberSession);

        if(memberName.isPresent()){
            Member member = memberName.get();
            member.setName(name);
            memberRepository.save(member);
            return member.getId();
        }

        return null;
    }

    //생일 변경
    public Long changeBirthday(String memberSession, LocalDate birtyday){
        Optional<Member> memberBirthday = findByEmail(memberSession);

        if(memberBirthday.isPresent()){
            Member member = memberBirthday.get();
            member.setBirthday(birtyday);
            memberRepository.save(member);
            return member.getId();
        }

        return null;
    }

}


