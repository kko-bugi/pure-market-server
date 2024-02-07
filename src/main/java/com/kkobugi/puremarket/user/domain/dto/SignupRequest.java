package com.kkobugi.puremarket.user.domain.dto;

import com.kkobugi.puremarket.user.domain.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "회원가입 요청")
public record SignupRequest(
        @Schema(description = "닉네임", example = "꼬부기")
        String nickname,
        @Schema(description = "로그인 아이디", example = "id")
        String loginId,
        @Schema(description = "비밀번호", example = "pwpwpw")
        String password,
        @Schema(description = "비밀번호 확인", example = "pwpwpw")
        String passwordCheck,
        @Schema(description = "연락처", example = "01012345678")
        String contact) {

    public User toUser(String encodedPassword, String imageUrl) {
        return User.builder()
                .nickname(this.nickname)
                .loginId(this.loginId)
                .password(encodedPassword)
                .contact(this.contact)
                .profileImage(imageUrl)
                .build();
    }
}
