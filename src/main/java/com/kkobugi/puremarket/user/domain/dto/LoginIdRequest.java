package com.kkobugi.puremarket.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "아이디 중복 체크 요청")
public record LoginIdRequest(
        @Schema(description = "로그인 아이디", example = "id")
        String loginId) {}
