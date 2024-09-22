package com.quailss.demo.controller;

import com.quailss.demo.domain.Member;
import com.quailss.demo.domain.Recipe;
import com.quailss.demo.domain.Review;
import com.quailss.demo.domain.dto.ReviewCommand;
import com.quailss.demo.domain.enums.MemberStatus;
import com.quailss.demo.service.AuthService;
import com.quailss.demo.service.RecipeService;
import com.quailss.demo.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final RecipeService recipeService;
    private final AuthService authService;

    @GetMapping("/recipe/{recipeId}") //특정 레시피에 대한 리뷰들
    public ResponseEntity<List<Review>> getReviews(@PathVariable Long recipeId){
        List<Review> reviewList = reviewService.findByRecipeId(recipeId);
        
        for(Review review : reviewList){
            if(review.getMember().getStatus() == MemberStatus.DEACTIVATED)
                review.getMember().setName("알수없음");
        }
        
        return ResponseEntity.ok(reviewList);
    }

    @GetMapping("/member/{memberId}")  //특정 회원이 작성한 리뷰들
    public ResponseEntity<List<Review>> getReviewsByMember(@PathVariable Long memberId){
        List<Review> reviewList = reviewService.findByMemberId(memberId);
        return ResponseEntity.ok(reviewList);
    }

    @PostMapping("/recipe/{recipeId}") //특정 레시피에 대해 리뷰 작성
    public ResponseEntity<String> writeReview(HttpSession session,
                                              @PathVariable Long recipeId,
                                              @RequestBody ReviewCommand reviewCommand){
        //유저 정보 가져오기 - 세션
        Member loggedInMember = authService.getLoggedInMember(session)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인하지 않은 사용자입니다."));

        Recipe existingRecipe = recipeService.getRecipe(recipeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "레시피를 찾을 수 없습니다."));

        reviewService.insertReview(existingRecipe, loggedInMember, reviewCommand.getScore(), reviewCommand.getContent());

        return ResponseEntity.ok("Review successfully saved");
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<Review> updateReview(HttpSession session,
                                               @PathVariable Long reviewId,
                                               @RequestBody ReviewCommand reviewCommand){
        Member loggedInMember = authService.getLoggedInMember(session)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인하지 않은 사용자입니다."));

        Review existingReview = reviewService.findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 리뷰입니다."));

        if (!existingReview.getMember().getId().equals(loggedInMember.getId())) {
            throw new AccessDeniedException("리뷰 작성자가 아닙니다.");
        }
        Review updatedReview = reviewService.updateReview(existingReview.getId(), reviewCommand);

        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(HttpSession session, @PathVariable Long reviewId){
        Member loggedInMember = authService.getLoggedInMember(session)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인하지 않은 사용자입니다."));

        Review review = reviewService.findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 리뷰입니다."));

        if(!review.getMember().getEmail().equals(loggedInMember.getEmail()))
            throw new AccessDeniedException("리뷰 작성자가 아닙니다.");

        try {
            reviewService.deleteReview(review);
            return ResponseEntity.ok("Review deleted successfully");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
