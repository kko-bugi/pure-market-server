package com.kkobugi.puremarket.user.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "마이페이지 응답")
public record MyPageResponse(
        @Schema(description = "닉네임", example = "꼬부기")
        String nickname,
        @Schema(description = "프로필 이미지", example = "https://dfjwijf")
        String profileImage,
        @Schema(description = "유저가 작성한 판매글 리스트")
        List<Produce> produceList,
        @Schema(description = "유저가 작성한 레시피글 리스트")
        List<Recipe> recipeList,
        @Schema(description = "유저가 작성한 나눔글 리스트")
        List<Giveaway> giveawayList) {
    public record Produce(
            @Schema(description = "판매글 Idx", example = "1")
            Long produceIdx,
            @Schema(description = "글 제목", example = "판매글")
            String title,
            @Schema(description = "글 이미지 url", example = "https://dwffwdfdwbwv")
            String produceImage,
            @Schema(description = "글 작성일자", example = "2023/02/02")
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd", timezone = "Asia/Seoul")
            LocalDate createdDate,
            @Schema(description = "판매 상태", example = "판매중")
            String status) {}

    public record Recipe(
            @Schema(description = "레시피글 Idx", example = "1")
            Long recipeIdx,
            @Schema(description = "글 제목", example = "레시피글")
            String title,
            @Schema(description = "글 이미지 url", example = "https://dwffwdfdwbwv")
            String recipeImage,
            @Schema(description = "글 작성일자", example = "2023/02/02")
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd", timezone = "Asia/Seoul")
            LocalDate createdDate) {}

    public record Giveaway(
            @Schema(description = "나눔글 Idx", example = "1")
            Long giveawayIdx,
            @Schema(description = "글 제목", example = "나눔글")
            String title,
            @Schema(description = "글 이미지 url", example = "https://dwffwdfdwbwv")
            String giveawayImage,
            @Schema(description = "글 작성일자", example = "2023/02/02")
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd", timezone = "Asia/Seoul")
            LocalDate createdDate,
            @Schema(description = "나눔 상태", example = "나눔완료")
            String status) {}
}
