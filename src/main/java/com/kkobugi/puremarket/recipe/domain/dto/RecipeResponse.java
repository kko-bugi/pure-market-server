package com.kkobugi.puremarket.recipe.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "레시피글 상세 응답")
public record RecipeResponse(
        @Schema(description = "레시피글 Idx", example = "1")
        Long recipeIdx,
        @Schema(description = "글 제목", example = "레시피글")
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
        String profileImage,
        @Schema(description = "글 작성자 여부", example = "true")
        boolean isWriter) {
    public record IngredientDto(
            @Schema(description = "재료명", example = "토마토")
            String name,
            @Schema(description = "재료 양", example = "한 알")
            String quantity) {}
    public record SauceDto(
            @Schema(description = "양념명", example = "소금")
            String name,
            @Schema(description = "양념 양", example = "한 꼬집")
            String quantity) {}
    public record RecipeDescriptionDto(
            @Schema(description = "조리 순서 번호", example = "1")
            Integer orderNumber,
            @Schema(description = "상세 설명", example = "재료를 함께 볶아준다.")
            String description) {}
}
