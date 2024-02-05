package com.kkobugi.puremarket.recipe.domain.dto;

import java.util.List;

public record RecipeListResponse(List<RecipeDto> recipeList) {
    public record RecipeDto(
            Long recipeIdx,
            String title,
            String recipeImage,
            String nickname,
            String profileImage) {
    }
}
