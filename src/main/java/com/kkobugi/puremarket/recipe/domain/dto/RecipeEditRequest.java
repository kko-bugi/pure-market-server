package com.kkobugi.puremarket.recipe.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record RecipeEditRequest(@Schema(description = "글 제목", example = "레시피글")
                                String title,
                                @Schema(description = "글 내용", example = "레시피글 내용")
                                String content,
                                @Schema(description = "재료 리스트")
                                List<IngredientDto> ingredientList,
                                @Schema(description = "양념 리스트")
                                List<SauceDto> sauceList,
                                @Schema(description = "조리 순서 리스트")
                                List<RecipeDescriptionDto> recipeDescriptionList) {}
