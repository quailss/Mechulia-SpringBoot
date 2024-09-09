package com.quailss.demo.service;

import com.quailss.demo.domain.Member;
import com.quailss.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Optional<Member> findById(Long member_id){
        return memberRepository.findById(member_id);
    }
}
