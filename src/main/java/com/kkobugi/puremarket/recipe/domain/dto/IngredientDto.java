package com.kkobugi.puremarket.recipe.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record IngredientDto(@Schema(description = "재료명", example = "토마토")
                            String name,
                            @Schema(description = "재료 양", example = "한 알")
                            String quantity) {
}
