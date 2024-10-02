package com.quailss.demo.service;

import com.quailss.demo.domain.Member;
import com.quailss.demo.domain.dto.ResponseMemberInfoDTO;
import com.quailss.demo.domain.enums.MemberStatus;
import com.quailss.demo.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final ReviewService reviewService;
    private final MemberRepository memberRepository;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;


    public Optional<Member> findById(Long member_id){
        return memberRepository.findById(member_id);
    }


    //탈퇴 요청 처리
    @Transactional
    public Long changeWithdrawalMemberstatus(HttpSession httpSession){
        Optional<Member> withdrawalMember = authService.getLoggedInMember(httpSession);

        if(withdrawalMember.isPresent()){
            Member member = withdrawalMember.get();
            member.setStatus(MemberStatus.DEACTIVATED);
            member.setDeletedAt(LocalDateTime.now());
            memberRepository.save(member);

            reviewService.updateMemberStatusByMemberId(member.getId(), MemberStatus.DEACTIVATED);
            return member.getId();
        }

        throw new NullPointerException();
    }

    //회원정보 가져오기
    public ResponseMemberInfoDTO getMemberInfoSession(HttpSession session){
        Member member  = authService.getLoggedInMember(session).get();
        return ResponseMemberInfoDTO.getMemberinfo(member.getEmail(), member.getName(), member.getPhonenumber(), member.getBirthday());
    }

    //회원정보 변경
    @Transactional
    public Long changeMemberInfo(ResponseMemberInfoDTO responseMemberInfoDTO, HttpSession session){

        Optional<Member> member = authService.getLoggedInMember(session);

        if(member.isPresent()){
            Member memberInfo = member.get();
            memberInfo.setName(responseMemberInfoDTO.getName());
            memberInfo.setPhonenumber(responseMemberInfoDTO.getPhoneNumber());
            memberInfo.setBirthday(responseMemberInfoDTO.getBirthday());
            memberRepository.save(memberInfo);

            return memberInfo.getId();
        }

        throw new NullPointerException();
    }

    public Long changePassword(HttpSession session ,String newPassword){

        Optional<Member> member = authService.getLoggedInMember(session);

        if(member.isPresent()){
            Member memberinfo = member.get();
            if(newPassword.equals("' '")){
                return memberinfo.getId();
            }else{
                memberinfo.setPassword(passwordEncoder.encode(newPassword).toString());
                memberRepository.save(memberinfo);
                return memberinfo.getId();
            }
        }

        throw new NullPointerException();
    }

}


