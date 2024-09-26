package com.quailss.demo.domain.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookmarkDto {
    private Long id;
    private Long memberId;
    private Long recipeId;
    private String recipeName;
    private String imgUrl;
}
