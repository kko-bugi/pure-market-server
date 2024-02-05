package com.kkobugi.puremarket.recipe.domain.dto;

import java.util.List;

public record RecipeResponse(Long recipeIdx,
                             String title,
                             String content,
                             String recipeImage,
                             List<IngredientDto> ingredientList,
                             List<SauceDto> sauceList,
                             List<RecipeDescriptionDto> recipeDescriptionList,
                             String nickname,
                             String contact,
                             String profileImage,
                             boolean isWriter) {
    public record IngredientDto(String name, String quantity) {}
    public record SauceDto(String name, String quantity) {}
    public record RecipeDescriptionDto(Integer orderNumber, String description) {}
}
