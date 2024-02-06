package com.kkobugi.puremarket.giveaway.domain.dto;

import java.util.List;

public record GiveawayListResponse(List<GiveawayDto> giveawayList) {
    public record GiveawayDto(
            Long giveawayIdx,
            String title,
            String content,
            String giveawayImage,
            String giveawayStatus) {
    }
}
