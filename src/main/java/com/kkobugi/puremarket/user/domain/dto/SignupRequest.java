package com.kkobugi.puremarket.user.domain.dto;

import com.kkobugi.puremarket.user.domain.entity.User;
import lombok.Builder;

@Builder
public record SignupRequest(
        String nickname,
        String loginId,
        String password,
        String passwordCheck,
        String contact) {
    public User toUser(String encodedPassword) {
        return User.builder()
                .loginId(this.loginId)
                .password(encodedPassword)
                .nickname(this.nickname)
                .contact(this.contact)
                .build();
    }
}
