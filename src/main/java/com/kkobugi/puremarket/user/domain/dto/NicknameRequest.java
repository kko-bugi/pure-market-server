package com.kkobugi.puremarket.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "닉네임 중복 체크 요청")
public record NicknameRequest(
        @Schema(description = "닉네임", example = "꼬부기")
        String nickname) {
}
