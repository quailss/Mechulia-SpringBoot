package com.quailss.demo.service;

import com.quailss.demo.domain.enums.MemberStatus;
import com.quailss.demo.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberCleanupService {
    private final MemberRepository memberRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void cleanupMembers(){
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        memberRepository.deleteByStatusAndDeletedAtBefore(MemberStatus.DEACTIVATED, thirtyDaysAgo);
    }
}
