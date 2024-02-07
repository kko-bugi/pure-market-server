package com.kkobugi.puremarket.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 요청")
public record LoginRequest(
        @Schema(description = "로그인 아이디", example = "id")
        String loginId,
        @Schema(description = "비밀번호", example = "pwpwpw")
        String password) {}