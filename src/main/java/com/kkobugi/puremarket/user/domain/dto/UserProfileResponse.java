package com.kkobugi.puremarket.user.domain.dto;

import java.time.LocalDate;
import java.util.List;

public record UserProfileResponse(
        String nickname,
        String profileImage,
        List<Produce> produceList,
        List<Recipe> recipeList,
        List<Giveaway> giveawayList) {
    public record Produce(
            Long produceIdx,
            String title,
            String produceImage,
            LocalDate createdDate) {
    }

    public record Recipe(
            Long recipeIdx,
            String title,
            String recipeImage,
            LocalDate createdDate) {
    }

    public record Giveaway(
            Long giveawayIdx,
            String title,
            String giveawayImage,
            LocalDate createdDate) {
    }
}
