package com.kkobugi.puremarket.giveaway.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "나눔글 목록 응답")
public record GiveawayListResponse(
        @Schema(description = "나눔글 리스트")
        List<GiveawayDto> giveawayList) {
    public record GiveawayDto(
            @Schema(description = "나눔글 Idx", example = "1")
            Long giveawayIdx,
            @Schema(description = "글 제목", example = "나눔글")
            String title,
            @Schema(description = "글 내용", example = "나눕니다.")
            String content,
            @Schema(description = "글 이미지 url", example = "https://dwffwdfdwbwv")
            String giveawayImage,
            @Schema(description = "나눔 상태", example = "나눔완료")
            String giveawayStatus) {}
}
