package com.quailss.demo.controller;

import com.quailss.demo.domain.Member;
import com.quailss.demo.domain.Recipe;
import com.quailss.demo.domain.Review;
import com.quailss.demo.domain.dto.ReviewCommand;
import com.quailss.demo.domain.enums.MemberStatus;
import com.quailss.demo.exception.EntityNotFoundException;
import com.quailss.demo.service.AuthService;
import com.quailss.demo.service.RecipeService;
import com.quailss.demo.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
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
        Member loggedInMember = authService.getLoggedInMember(session)
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("로그인하지 않은 사용자입니다."));
        try {
            reviewService.insertReview(recipeId, loggedInMember, reviewCommand.getScore(), reviewCommand.getContent());
            return ResponseEntity.ok("Review successfully saved");
        }catch (EntityNotFoundException.RecipeNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<Review> updateReview(HttpSession session,
                                               @PathVariable Long reviewId,
                                               @RequestBody ReviewCommand reviewCommand){
        Member loggedInMember = authService.getLoggedInMember(session)
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("로그인하지 않은 사용자입니다."));
        try {
            Review updatedReview = reviewService.updateReview(reviewId, loggedInMember, reviewCommand);
            return ResponseEntity.ok(updatedReview);
        }catch (EntityNotFoundException.RecipeNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(HttpSession session, @PathVariable Long reviewId){
        Member loggedInMember = authService.getLoggedInMember(session)
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("로그인하지 않은 사용자입니다."));

        try {
            reviewService.deleteReview(reviewId, loggedInMember);
            return ResponseEntity.ok("Review deleted successfully");
        }catch(EntityNotFoundException.ReviewNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
