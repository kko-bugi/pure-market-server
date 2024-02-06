package com.kkobugi.puremarket.giveaway.domain.dto;

public record GiveawayResponse(Long giveawayIdx,
                               String title,
                               String content,
                               String giveawayImage,
                               String giveawayStatus,
                               String nickname,
                               String contact,
                               String profileImage,
                               boolean isWriter) {
}
