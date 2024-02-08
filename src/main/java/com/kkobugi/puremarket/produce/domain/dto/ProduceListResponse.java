package com.kkobugi.puremarket.produce.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "판매글 목록 응답")
public record ProduceListResponse(
        @Schema(description = "판매글 리스트")
        List<ProduceDto> produceList) {
    public record ProduceDto(
            @Schema(description = "판매글 Idx", example = "1")
            Long produceIdx,
            @Schema(description = "글 제목", example = "판매글")
            String title,
            @Schema(description = "가격", example = "10000")
            Integer price,
            @Schema(description = "글 이미지 url", example = "https://dwffwdfdwbwv")
            String produceImage,
            @Schema(description = "판매 상태", example = "판매완료")
            String produceStatus) {}
}
