package com.quailss.demo.controller;

import com.quailss.demo.domain.Recipe;
import com.quailss.demo.domain.dto.RecipeDto;
import com.quailss.demo.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recipes")
public class RecipeController {
    private final RecipeService recipeService;

    @GetMapping
    public ResponseEntity<RecipeDto> getRecipes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {
        Page<Recipe> recipesPage = recipeService.getRecipes(page, size);
        long totalRecipesCnt = recipesPage.getTotalElements();

        RecipeDto response = new RecipeDto(
                recipesPage.getContent(),  // 실제 Recipe 리스트
                totalRecipesCnt  // 전체 Recipe 개수
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{menuId}")
    public ResponseEntity<RecipeDto> getRecipesByCategory(
            @PathVariable Long menuId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {
        Page<Recipe> recipesPage = recipeService.getRecipesByCategory(menuId, page, size);
        long totalRecipesCnt = recipesPage.getTotalElements();

        RecipeDto response = new RecipeDto(
                recipesPage.getContent(),  // 실제 Recipe 리스트
                totalRecipesCnt  // 전체 Recipe 개수
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/theme")
    public ResponseEntity<RecipeDto> getRecipesByTheme(@RequestParam String keyword,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "15") int size){

        Page<Recipe> recipesPage = recipeService.getRecipesByMenuIdAndKeyword(Long.valueOf(0), keyword, page, size);
        long totalRecipesCnt = recipesPage.getTotalElements();

        RecipeDto response = new RecipeDto(
                recipesPage.getContent(),  // 실제 Recipe 리스트
                totalRecipesCnt  // 전체 Recipe 개수
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<RecipeDto> getRecipesByKeyword(@RequestParam Long menuId,
                                                   @RequestParam String keyword,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "15") int size){
        Page<Recipe> recipesPage = recipeService.getRecipesByMenuIdAndKeyword(menuId, keyword, page, size);
        long totalRecipesCnt = recipesPage.getTotalElements();

        RecipeDto response = new RecipeDto(
                recipesPage.getContent(),  // 실제 Recipe 리스트
                totalRecipesCnt  // 전체 Recipe 개수
        );

        return ResponseEntity.ok(response);
    }
}
