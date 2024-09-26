package com.quailss.demo.repository;

import com.quailss.demo.domain.Member;
import com.quailss.demo.domain.enums.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByPhonenumber(String phonenumber);

    void deleteByStatusAndDeletedAtBefore(MemberStatus memberStatus, LocalDateTime thirtyDaysAgo);
}
