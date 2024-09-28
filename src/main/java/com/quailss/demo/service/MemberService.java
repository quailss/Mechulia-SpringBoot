package com.quailss.demo.service;

import com.quailss.demo.domain.Member;
import com.quailss.demo.domain.enums.MemberStatus;
import com.quailss.demo.domain.enums.Provider;
import com.quailss.demo.repository.AuthRepository;
import com.quailss.demo.repository.MemberRepository;
import com.quailss.demo.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final AuthRepository authRepository;
    private final ReviewRepository reviewRepository;

    public Optional<Member> findById(Long member_id){
        return memberRepository.findById(member_id);
    }
    // 휴대폰 번호 변경
    public String changePhoneNumber(String memberSession, String phoneNumber, Provider provider){
        Optional<Member> memberPhoneNumber = authRepository.findByEmailAndProvider(memberSession, provider);

        if(memberPhoneNumber.isPresent()){
            Member member = memberPhoneNumber.get();
            member.setPhonenumber(phoneNumber);
            memberRepository.save(member);
            return member.getPhonenumber();
        }

        throw new NullPointerException();
    }

    // 이름 변경
    public Long changeName(String memberSession, String name, Provider provider){
        Optional<Member> memberName = authRepository.findByEmailAndProvider(memberSession, provider);

        if(memberName.isPresent()){
            Member member = memberName.get();
            member.setName(name);
            memberRepository.save(member);
            return member.getId();
        }

        throw new NullPointerException();
    }

    //생일 변경
    public Long changeBirthday(String memberSession, LocalDate birtyday, Provider provider){
        Optional<Member> memberBirthday = authRepository.findByEmailAndProvider(memberSession, provider);

        if(memberBirthday.isPresent()){
            Member member = memberBirthday.get();
            member.setBirthday(birtyday);
            memberRepository.save(member);
            return member.getId();
        }

        throw new NullPointerException();
    }

    //탈퇴 요청 처리
    public Long changeWithdrawalMemberstatus(String memberSesstion, Provider provider){
        Optional<Member> withdrawalMember = authRepository.findByEmailAndProvider(memberSesstion,provider);

        if(withdrawalMember.isPresent()){
            Member member = withdrawalMember.get();
            member.setStatus(MemberStatus.DEACTIVATED);
            memberRepository.save(member);

            reviewRepository.updateMemberStatusByMemberId(member.getId(), MemberStatus.DEACTIVATED);
            return member.getId();
        }

        throw new NullPointerException();
    }

}


