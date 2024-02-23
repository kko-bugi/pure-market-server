package com.kkobugi.puremarket.produce.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "판매글 수정 화면 조회 응답")
public record ProduceEditViewResponse(@Schema(description = "글 제목", example = "판매글")
                                      String title,
                                      @Schema(description = "글 내용", example = "판매함니다~")
                                      String content,
                                      @Schema(description = "가격", example = "10000")
                                      Integer price,
                                      @Schema(description = "글 이미지 url", example = "https://dwffwdfdwbwv")
                                      String produceImage,
                                      @Schema(description = "닉네임", example = "꼬부기")
                                      String nickname,
                                      @Schema(description = "연락처", example = "01012345678")
                                      String contact,
                                      @Schema(description = "프로필 이미지 url", example = "https://dwffwdfdwbwv")
                                      String profileImage) {
}
