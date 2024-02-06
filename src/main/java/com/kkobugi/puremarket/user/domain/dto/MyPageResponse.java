package com.kkobugi.puremarket.user.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;

public record MyPageResponse(
        String nickname,
        String profileImage,
        List<Produce> produceList,
        List<Recipe> recipeList,
        List<Giveaway> giveawayList) {
    public record Produce(
            Long produceIdx,
            String title,
            String produceImage,
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd", timezone = "Asia/Seoul")
            LocalDate createdDate,
            String status) {
    }

    public record Recipe(
            Long recipeIdx,
            String title,
            String recipeImage,
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd", timezone = "Asia/Seoul")
            LocalDate createdDate) {
    }

    public record Giveaway(
            Long giveawayIdx,
            String title,
            String giveawayImage,
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd", timezone = "Asia/Seoul")
            LocalDate createdDate,
            String status) {
    }
}
