package com.kkobugi.puremarket.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "토큰 재발급 요청")
public record ReissueTokenRequest(
        @Schema(description = "리프레쉬 토큰", example = "eyfsdkjkwjnv")
        String refreshToken) {}
