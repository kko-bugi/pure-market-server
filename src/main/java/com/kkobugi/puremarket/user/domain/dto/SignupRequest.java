package com.kkobugi.puremarket.user.domain.dto;

import com.kkobugi.puremarket.user.domain.entity.User;
import lombok.Builder;

@Builder
public record SignupRequest(
        String nickname,
        String loginId,
        String password,
        String passwordCheck,
        String contact) { //TODO: 프로필 이미지 추가
    public User toUser(String encodedPassword) {
        return User.builder()
                .nickname(this.nickname)
                .loginId(this.loginId)
                .password(encodedPassword)
                .contact(this.contact)
                .build();
    }
}
