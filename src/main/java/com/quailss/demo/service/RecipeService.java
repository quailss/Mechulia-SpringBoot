package com.quailss.demo.service;

import com.quailss.demo.domain.Recipe;
import com.quailss.demo.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;

    public Page<Recipe> getRecipes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        return recipeRepository.findAll(pageable);
    }

    public Page<Recipe> getRecipesByMenuIdAndKeyword(Long menuId, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        String[] nations = new String[]{"한국","서양","중국","일본","미국","이탈리아","동남아","프랑스","멕시코","독일","인도"};
        String[] menuArr = new String[]{"한식","양식","중식","일식","미국식","이탈리아식","동남아식","프랑스식","멕시코식","독일식","인도식"};

        int idx = -1;
        for(int i=0;i<menuArr.length;i++)
            if(keyword.equals(menuArr[i]) || keyword.equals(nations[i])){
                idx = i+1;
                break;
            }

        if(idx != -1){
            return recipeRepository.findAllByMenuId(Long.valueOf(idx), pageable);
        }else{
            if(menuId == 0)
                return recipeRepository.findAllByKeyword(keyword, pageable);
            else if(menuId > 0 && menuId < 5)
                return recipeRepository.findAllByMenuIdAndKeyword(menuId, keyword, pageable);
            else
                return recipeRepository.findAllByMenuIdAndKeyword(keyword, pageable);
        }
    }

    public Optional<Recipe> getRecipe(Long recipeId){
        return recipeRepository.findById(recipeId);
    }

    public Page<Recipe> getRecipesByCategory(Long menuId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        if(menuId < 5)
            return recipeRepository.findAllByMenuId(menuId, pageable);
        else
            return recipeRepository.findAllByMenuId(pageable);
    }

    public void save(Recipe recipe) {
        recipeRepository.save(recipe);
    }
}
