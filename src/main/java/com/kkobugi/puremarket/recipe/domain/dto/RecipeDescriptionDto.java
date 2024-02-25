package com.kkobugi.puremarket.recipe.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record RecipeDescriptionDto(@Schema(description = "조리 순서 번호", example = "1")
                                   Integer orderNumber,
                                   @Schema(description = "상세 설명", example = "재료를 함께 볶아준다.")
                                   String description) {
}
