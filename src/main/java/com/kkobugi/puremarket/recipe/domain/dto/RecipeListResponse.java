package com.kkobugi.puremarket.recipe.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
@Schema(description = "레시피글 목록 응답")
public record RecipeListResponse(
        @Schema(description = "레시피글 리스트")
        List<RecipeDto> recipeList) {
    public record RecipeDto(
            @Schema(description = "레시피글 Idx", example = "1")
            Long recipeIdx,
            @Schema(description = "글 제목", example = "레시피글")
            String title,
            @Schema(description = "글 이미지 url", example = "https://dwffwdfdwbwv")
            String recipeImage,
            @Schema(description = "닉네임", example = "꼬부기")
            String nickname,
            @Schema(description = "프로필 이미지 url", example = "https://dwffwdfdwbwv")
            String profileImage) {
    }
}
