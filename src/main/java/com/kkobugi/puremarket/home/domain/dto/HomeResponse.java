package com.kkobugi.puremarket.home.domain.dto;

import java.util.List;

public record HomeResponse(List<ProduceDto> produceList,
                           List<RecipeDto> recipeList,
                           List<GiveawayDto> giveawayList) {
    public record ProduceDto(Long produceIdx,
                             String title,
                             Integer price,
                             String produceImage){}

    public record RecipeDto(Long recipeIdx,
                            String title,
                            String recipeImage){}

    public record GiveawayDto(Long giveawayIdx,
                              String title,
                              String content,
                              String giveawayImage){}
}
