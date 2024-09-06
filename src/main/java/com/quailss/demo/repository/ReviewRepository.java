package com.quailss.demo.repository;

import com.quailss.demo.domain.Member;
import com.quailss.demo.domain.Review;
import com.quailss.demo.domain.enums.Provider;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByRecipeId(Long recipeId);

    List<Review> findByMemberId(Long memberId);
}
