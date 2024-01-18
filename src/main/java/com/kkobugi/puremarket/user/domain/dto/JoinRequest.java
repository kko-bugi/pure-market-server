package com.kkobugi.puremarket.user.domain.dto;

import com.kkobugi.puremarket.user.domain.entity.User;
import lombok.Builder;

@Builder
public record JoinRequest(
        String nickname,
        String loginId,
        String password,
        String contact) {
    public User toUser() {
        return User.builder()
                .loginId(this.loginId)
                .password(this.password)
                .nickname(this.nickname)
                .contact(this.contact)
                .build();
    }
}
