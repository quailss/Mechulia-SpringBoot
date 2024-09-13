package com.quailss.demo.repository;

import com.quailss.demo.domain.Review;
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
    @Query("UPDATE Review r SET r.score = :score, r.content = :content WHERE r.id = :reviewId")
    Review updateReview(@Param("id") Long reviewId, @Param("score") BigDecimal score, @Param("content") String content);

}
