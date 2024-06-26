package com.kkobugi.puremarket.recipe.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "레시피글 수정 화면 응답")
public record RecipeEditViewResponse(@Schema(description = "글 제목", example = "레시피글")
                                     String title,
                                     @Schema(description = "글 내용", example = "레시피글 내용")
                                     String content,
                                     @Schema(description = "글 이미지 url", example = "https://dwffwdfdwbwv")
                                     String recipeImage,
                                     @Schema(description = "재료 리스트")
                                     List<IngredientDto> ingredientList,
                                     @Schema(description = "양념 리스트")
                                     List<SauceDto> sauceList,
                                     @Schema(description = "조리 순서 리스트")
                                     List<RecipeDescriptionDto> recipeDescriptionList,
                                     @Schema(description = "닉네임", example = "꼬부기")
                                     String nickname,
                                     @Schema(description = "연락처", example = "010112345678")
                                     String contact,
                                     @Schema(description = "프로필 이미지 url", example = "https://dwffwdfdwbwv")
                                     String profileImage) {}
