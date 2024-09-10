package com.quailss.demo.domain.dto;

import com.quailss.demo.domain.Recipe;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecipeDto {
    private List<Recipe> recipes;
    private long totalElements;
}
