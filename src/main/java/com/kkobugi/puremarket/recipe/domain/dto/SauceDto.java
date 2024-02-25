package com.kkobugi.puremarket.recipe.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record SauceDto(@Schema(description = "양념명", example = "소금")
                       String name,
                       @Schema(description = "양념 양", example = "한 꼬집")
                       String quantity) {
}
