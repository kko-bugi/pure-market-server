package com.kkobugi.puremarket.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "유저 프로필 응답")
public record ProfileResponse(
        @Schema(description = "닉네임", example = "꼬부기")
        String nickname,
        @Schema(description = "연락처", example = "01012345678")
        String contact,
        @Schema(description = "프로필 이미지", example = "https://dfjwijf")
        String profileImage) {}
