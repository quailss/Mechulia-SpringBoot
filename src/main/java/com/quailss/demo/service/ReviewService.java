package com.quailss.demo.service;

import com.quailss.demo.domain.Member;
import com.quailss.demo.domain.Recipe;
import com.quailss.demo.domain.Review;
import com.quailss.demo.domain.dto.ReviewCommand;
import com.quailss.demo.domain.dto.ReviewDto;
import com.quailss.demo.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public Optional<Review> findById(Long reviewId) {
        return reviewRepository.findById(reviewId);
    }

    public List<Review> findByRecipeId(Long recipeId) {
        return reviewRepository.findByRecipeId(recipeId);
    }

    public List<Review> findByMemberId(Long memberId) {
        return reviewRepository.findByMemberId(memberId);
    }

    public void insertReview(Recipe recipe, Member member, BigDecimal score, String content) {
        Review newReview = Review.builder()
                .member(member)
                .recipe(recipe)
                .score(score)
                .content(content)
                .build();
        reviewRepository.save(newReview);
    }

    public Review updateReview(ReviewDto reviewDto) {
        return reviewRepository.updateReview(reviewDto.getReview_id(), reviewDto.getScore(), reviewDto.getContent());
    }

    public void deleteReview(Review review) {
        reviewRepository.delete(review);
    }
}
