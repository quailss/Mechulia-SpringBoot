package com.quailss.demo.service;

import com.quailss.demo.domain.Member;
import com.quailss.demo.domain.Recipe;
import com.quailss.demo.domain.Review;
import com.quailss.demo.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public List<Review> findByRecipeId(Long recipeId) {
        return reviewRepository.findByRecipeId(recipeId);
    }

    public List<Review> findByMemberId(Long memberId) {
        return reviewRepository.findByMemberId(memberId);
    }

    public void insertReview(Recipe recipe, Member member, BigDecimal score, String content) {
        Review newReview = Review.builder()
                .score(score)
                .content(content)
                .member(member)
                .recipe(recipe)
                .build();

        reviewRepository.save(newReview);
    }
}
