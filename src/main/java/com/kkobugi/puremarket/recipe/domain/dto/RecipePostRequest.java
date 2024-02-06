package com.kkobugi.puremarket.recipe.domain.dto;

import java.util.List;

public record RecipePostRequest(String title,
                                String content,
                                List<RecipeResponse.IngredientDto> ingredientList,
                                List<RecipeResponse.SauceDto> sauceList,
                                List<RecipeResponse.RecipeDescriptionDto> recipeDescriptionList) {
}
