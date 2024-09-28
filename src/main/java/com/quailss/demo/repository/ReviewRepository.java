package com.quailss.demo.repository;

import com.quailss.demo.domain.Review;
import com.quailss.demo.domain.enums.MemberStatus;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByRecipeId(Long recipeId);

    List<Review> findByMemberId(Long memberId);

    @Modifying
    @Query("UPDATE Review r SET r.memberStatus = :status WHERE r.memberId = :memberId")
    void updateMemberStatusByMemberId(Long memberId, MemberStatus status);
}
