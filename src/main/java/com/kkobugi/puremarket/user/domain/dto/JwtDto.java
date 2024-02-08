package com.kkobugi.puremarket.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "토큰 응답")
public record JwtDto(
        @Schema(description = "액세스 토큰", example = "eysdflwjfjl")
        String accessToken,
        @Schema(description = "리프레쉬 토큰", example = "ehlrsblfjnv")
        String refreshToken) {
}
