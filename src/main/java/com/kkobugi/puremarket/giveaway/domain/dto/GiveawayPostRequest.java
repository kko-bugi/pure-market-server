package com.kkobugi.puremarket.giveaway.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "나눔글 등록 요청")
public record GiveawayPostRequest(
        @Schema(description = "글 제목", example = "나눔글")
        String title,
        @Schema(description = "글 내용", example = "나눕니다.")
        String content) {}
