package com.quailss.demo.controller;

import com.quailss.demo.domain.Member;
import com.quailss.demo.domain.Recipe;
import com.quailss.demo.domain.Review;
import com.quailss.demo.service.MemberService;
import com.quailss.demo.service.RecipeService;
import com.quailss.demo.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    /*
    @PostMapping("/createReview/{recipe_id}") //특정 레시피에 대해 리뷰 작성
    public ResponseEntity<String> insertReview(@PathVariable Long recipe_id,
                                               @RequestParam BigDecimal score,
                                               @RequestParam String content){
        //유저 정보 가져오기 - 세션
        Long member_id = Long.valueOf(1);

        Optional<Recipe> recipeOptional = recipeService.getRecipe(recipe_id);
        Optional<Member> memberOptional = memberService.findById(member_id);

        if(memberOptional.isEmpty())
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        if(recipeOptional.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "레시피를 찾을 수 없습니다.");

        reviewService.insertReview(recipeOptional.get(), memberOptional.get(), score, content);

        return ResponseEntity.ok("success");
    }
    */
}
