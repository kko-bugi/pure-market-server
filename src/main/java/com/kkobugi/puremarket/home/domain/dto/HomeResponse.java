package com.kkobugi.puremarket.home.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "홈화면 응답")
public record HomeResponse(
        @Schema(description = "최근 판매글 리스트")
        List<ProduceDto> produceList,
        @Schema(description = "최근 레시피글 리스트")
        List<RecipeDto> recipeList,
        @Schema(description = "최근 나눔글 리스트")
        List<GiveawayDto> giveawayList) {
    public record ProduceDto(
            @Schema(description = "판매글 Idx", example = "1")
            Long produceIdx,
            @Schema(description = "글 제목", example = "판매글")
            String title,
            @Schema(description = "가격", example = "10000")
            Integer price,
            @Schema(description = "글 이미지 url", example = "https://dwffwdfdwbwv")
            String produceImage){}

    public record RecipeDto(
            @Schema(description = "레시피글 Idx", example = "1")
            Long recipeIdx,
            @Schema(description = "글 제목", example = "레시피글")
            String title,
            @Schema(description = "글 이미지 url", example = "https://dwffwdfdwbwv")
            String recipeImage){}

    public record GiveawayDto(
            @Schema(description = "나눔 Idx", example = "1")
            Long giveawayIdx,
            @Schema(description = "글 제목", example = "나눔글")
            String title,
            @Schema(description = "글 내용", example = "나눔합니다")
            String content,
            @Schema(description = "글 이미지 url", example = "https://dwffwdfdwbwv")
            String giveawayImage){}
}
