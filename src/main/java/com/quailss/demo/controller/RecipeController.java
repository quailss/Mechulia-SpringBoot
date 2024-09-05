package com.quailss.demo.controller;

import com.quailss.demo.domain.Recipe;
import com.quailss.demo.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recipe")
public class RecipeController {
    private final RecipeService recipeService;

    @GetMapping("/main")
    public ResponseEntity<Page<Recipe>> getRecipes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {
        Page<Recipe> recipes = recipeService.getRecipes(page, size);
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/search/{menu_id}")
    public ResponseEntity<Page<Recipe>> getRecipes(@PathVariable("menu_id") Long menu_id,
                                                   @RequestParam String keyword,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "15") int size){
        Page<Recipe> recipes = recipeService.getRecipesByMenu_idAndKeyword(menu_id, keyword, page, size);
        return ResponseEntity.ok(recipes);
    }
}
