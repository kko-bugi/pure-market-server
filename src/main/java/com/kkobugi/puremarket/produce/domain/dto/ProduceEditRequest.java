package com.kkobugi.puremarket.produce.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "판매글 수정 요청")
public record ProduceEditRequest(@Schema(description = "글 제목", example = "판매글")
                                 String title,
                                 @Schema(description = "글 내용", example = "판매함니다~")
                                 String content,
                                 @Schema(description = "가격", example = "10000")
                                 Integer price,
                                 @Schema(description = "글 이미지 url", example = "https://dwffwdfdwbwv")
                                 String produceImage) {
}
