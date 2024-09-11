package com.quailss.demo.controller;

import com.quailss.demo.domain.Member;
import com.quailss.demo.domain.Recipe;
import com.quailss.demo.domain.Review;
import com.quailss.demo.domain.dto.ReviewCommand;
import com.quailss.demo.domain.dto.ReviewDto;
import com.quailss.demo.service.MemberService;
import com.quailss.demo.service.RecipeService;
import com.quailss.demo.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {
    private final ReviewService reviewService;
    private final MemberService memberService;
    private final RecipeService recipeService;

    @GetMapping("/recipe/{recipe_id}") //특정 레시피에 대한 리뷰들
    public ResponseEntity<List<Review>> getReviews(@PathVariable Long recipe_id){
        List<Review> reviewList = reviewService.findByRecipeId(recipe_id);
        return ResponseEntity.ok(reviewList);
    }

    @GetMapping("/member/{member_id}")  //특정 회원이 작성한 리뷰들
    public ResponseEntity<List<Review>> getReviewsByMember(@PathVariable Long member_id){
        List<Review> reviewList = reviewService.findByMemberId(member_id);
        return ResponseEntity.ok(reviewList);
    }

    @PostMapping("/write") //특정 레시피에 대해 리뷰 작성
    public ResponseEntity<String> writeReview(HttpSession session,
                                              @RequestBody ReviewCommand reviewCommand){
        //유저 정보 가져오기 - 세션
        String loggedinEmail = (String) session.getAttribute("Email");

        Optional<Recipe> recipeOptional = recipeService.getRecipe(reviewCommand.getRecipe_id());
        Optional<Member> memberOptional = memberService.findByEmail(loggedinEmail);

        if(memberOptional.isEmpty())
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        if(recipeOptional.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "레시피를 찾을 수 없습니다.");

        reviewService.insertReview(recipeOptional.get(), memberOptional.get(), reviewCommand.getScore(), reviewCommand.getContent());

        return ResponseEntity.ok("Review successfully saved");
    }

    @PutMapping("/update")
    public ResponseEntity<Review> updateReview(HttpSession session,
                                               @RequestBody ReviewDto reviewDto){
        //유저 정보 가져오기 - 세션
        String loggedinEmail = (String) session.getAttribute("Email");

        Optional<Review> reviewOptinal = reviewService.findById(reviewDto.getReview_id());
        if(reviewOptinal.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 리뷰입니다.");

        Optional<Recipe> recipeOptional = recipeService.getRecipe(reviewOptinal.get().getRecipe().getId());
        Optional<Member> memberOptional = memberService.findByEmail(loggedinEmail);

        if(memberOptional.isEmpty())
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        if(recipeOptional.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "레시피를 찾을 수 없습니다.");

        Review updatedReview = reviewService.updateReview(reviewDto);

        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteReview(HttpSession session, @RequestParam Long reviewId){
        String loggedinEmail = (String) session.getAttribute("Email");

        Review review = reviewService.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if(!review.getMember().getEmail().equals(loggedinEmail))
            throw new AccessDeniedException("리뷰 작성자가 아닙니다.");

        try {
            reviewService.deleteReview(review);
            return ResponseEntity.ok("Review deleted successfully");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
