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
            String title,
            String produceImage) {
            //LocalDate createdDate) {
    }

    public record Recipe(
            String title,
            String RecipeImage) {
            //LocalDate createdDate) {
    }

    public record Giveaway(
            String title,
            String GiveawayImage) {
    }
}
