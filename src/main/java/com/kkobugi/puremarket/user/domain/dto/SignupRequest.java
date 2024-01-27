package com.kkobugi.puremarket.user.domain.dto;

import com.kkobugi.puremarket.user.domain.entity.User;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record SignupRequest(
        String nickname,
        String loginId,
        String password,
        String passwordCheck,
        String contact,
        MultipartFile profileImage) {
    public User toUser(String encodedPassword) {
        return User.builder()
                .nickname(this.nickname)
                .loginId(this.loginId)
                .password(encodedPassword)
                .contact(this.contact)
                .build();
    }
}
