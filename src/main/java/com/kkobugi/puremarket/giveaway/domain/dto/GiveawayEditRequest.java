package com.kkobugi.puremarket.giveaway.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
@Schema(description = "나눔글 수정 요청")
public record GiveawayEditRequest(@Schema(description = "글 제목", example = "판매글")
                                  String title,
                                  @Schema(description = "글 내용", example = "판매함니다~")
                                  String content) {
}
