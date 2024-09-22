package com.quailss.demo.service;

import com.quailss.demo.domain.Member;
import com.quailss.demo.domain.Recipe;
import com.quailss.demo.domain.Review;
import com.quailss.demo.domain.dto.ReviewCommand;
import com.quailss.demo.exception.EntityNotFoundException;
import com.quailss.demo.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final RecipeService recipeService;

    public Optional<Review> findById(Long reviewId) {
        return reviewRepository.findById(reviewId);
    }

    public List<Review> findByRecipeId(Long recipeId) {
        return reviewRepository.findByRecipeId(recipeId);
    }

    public List<Review> findByMemberId(Long memberId) {
        return reviewRepository.findByMemberId(memberId);
    }

    public void insertReview(Long recipeId, Member member, BigDecimal score, String content) {
        Recipe recipe = recipeService.getRecipe(recipeId)
                .orElseThrow(() -> new EntityNotFoundException.RecipeNotFoundException("레시피를 찾을 수 없습니다."));

        Review newReview = Review.builder()
                .member(member)
                .recipe(recipe)
                .score(score)
                .content(content)
                .build();
        reviewRepository.save(newReview);
    }

    public Review updateReview(Long reviewId, Member loggedInMember, ReviewCommand reviewCommand) {
        Review existingReview = findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException.ReviewNotFoundException("존재하지 않는 리뷰입니다."));

        if (!existingReview.getMember().getId().equals(loggedInMember.getId())) {
            throw new AccessDeniedException("리뷰 작성자가 아닙니다.");
        }

        return reviewRepository.updateReview(reviewId, reviewCommand.getScore(), reviewCommand.getContent());
    }

    public void deleteReview(Long reviewId, Member loggedInMember) {
        Review review = findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException.ReviewNotFoundException("존재하지 않는 리뷰입니다."));

        if(!review.getMember().getEmail().equals(loggedInMember.getEmail()))
            throw new AccessDeniedException("리뷰 작성자가 아닙니다.");

        reviewRepository.delete(review);
    }
}
