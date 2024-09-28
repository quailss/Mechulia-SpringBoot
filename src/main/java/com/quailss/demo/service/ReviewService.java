package com.quailss.demo.service;

import com.quailss.demo.domain.Member;
import com.quailss.demo.domain.Recipe;
import com.quailss.demo.domain.Review;
import com.quailss.demo.domain.dto.ReviewCommand;
import com.quailss.demo.domain.dto.ReviewDto;
import com.quailss.demo.exception.EntityNotFoundException;
import com.quailss.demo.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final RecipeService recipeService;

    public Optional<Review> findById(Long reviewId) {
        return reviewRepository.findById(reviewId);
    }

    public List<ReviewDto> findByRecipeId(Long recipeId) {
        List<Review> reviews = reviewRepository.findByRecipeId(recipeId);
        return reviews.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ReviewDto> findByMemberId(Long memberId) {
        List<Review> reviews = reviewRepository.findByMemberId(memberId);
        return reviews.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void insertReview(Long recipeId, Member member, BigDecimal score, String content) {
        Recipe recipe = recipeService.getRecipe(recipeId)
                .orElseThrow(() -> new EntityNotFoundException.RecipeNotFoundException("레시피를 찾을 수 없습니다."));

        Review newReview = Review.builder()
                .memberId(member.getId())
                .memberName(member.getName())
                .memberStatus(member.getStatus())
                .recipe(recipe)
                .score(score)
                .content(content)
                .build();
        reviewRepository.save(newReview);

        updateRecipeAvg(recipe);
    }

    @Transactional
    public void updateReview(Long reviewId, Member loggedInMember, ReviewCommand reviewCommand) {
        Review existingReview = findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException.ReviewNotFoundException("존재하지 않는 리뷰입니다."));

        if(existingReview.getMemberId() != loggedInMember.getId()) {
            throw new AccessDeniedException("리뷰 작성자가 아닙니다.");
        }
        existingReview.setScore(reviewCommand.getScore());
        existingReview.setContent(reviewCommand.getContent());

        updateRecipeAvg(existingReview.getRecipe());
        reviewRepository.save(existingReview);
    }

    @Transactional
    public void deleteReview(Long reviewId, Member loggedInMember) {
        Review review = findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException.ReviewNotFoundException("존재하지 않는 리뷰입니다."));

        if(review.getMemberId() != loggedInMember.getId())
            throw new AccessDeniedException("리뷰 작성자가 아닙니다.");

        reviewRepository.delete(review);
        updateRecipeAvg(review.getRecipe());
    }

    private ReviewDto convertToDto(Review review) {
        return new ReviewDto(
                review.getId(),
                review.getScore(),
                review.getContent(),
                review.getCreatedAt(),
                review.getUpdatedAt(),
                review.getMemberId(),
                review.getMemberName(),
                review.getMemberStatus(),
                review.getRecipe().getId(),
                review.getRecipe().getName(),
                review.getRecipe().getImage_url()
        );
    }

    private void updateRecipeAvg(Recipe recipe) {
        List<ReviewDto> reviews = findByRecipeId(recipe.getId());
        int reviewCnt = reviews.size();
        BigDecimal newAvg;
        if(reviewCnt == 0)
            newAvg = BigDecimal.ZERO;
        else{
            // 모든 리뷰의 점수를 더한 후 평균 계산
            BigDecimal totalScore = reviews.stream()
                    .map(ReviewDto::getScore)
                    .reduce(BigDecimal.ZERO, BigDecimal::add); // 리뷰의 총합 계산
            newAvg = totalScore.divide(BigDecimal.valueOf(reviewCnt), 2, RoundingMode.HALF_UP); // 새로운 평균 계산
        }

        recipe.setAverage(newAvg);

        recipeService.save(recipe);
    }

}
