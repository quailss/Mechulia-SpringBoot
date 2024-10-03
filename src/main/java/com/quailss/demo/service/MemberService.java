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
        return ResponseMemberInfoDTO.getMemberinfo(member.getEmail(), member.getName(), member.getPhonenumber(), member.getBirthday(), member.getProvider());
    }

    //회원정보 변경
    @Transactional
    public void changeMemberInfo(ResponseMemberInfoDTO responseMemberInfoDTO, HttpSession session){
        Optional<Member> member = authService.getLoggedInMember(session);

        if(member.isPresent()){
            Member memberInfo = member.get();
            memberInfo.setName(responseMemberInfoDTO.getName());
            memberInfo.setPhonenumber(responseMemberInfoDTO.getPhoneNumber());
            memberInfo.setBirthday(responseMemberInfoDTO.getBirthday());

            memberInfo = changePassword(memberInfo,responseMemberInfoDTO.getPassword());

            memberRepository.save(memberInfo);
        }
    }

    //비밀번호 수정
    public Member changePassword(Member memberInfo,String newPassword){
        if(newPassword.trim().length()!=0){
            if(newPassword.matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")){
                memberInfo.setPassword(passwordEncoder.encode(newPassword).toString());
                return memberInfo;
            }else
                throw new IllegalArgumentException("유효한 비밀번호를 입력해주세요");
        }

        return memberInfo;
    }

}


