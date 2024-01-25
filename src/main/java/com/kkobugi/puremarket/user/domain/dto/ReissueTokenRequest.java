package com.kkobugi.puremarket.user.domain.dto;

public record ReissueTokenRequest(String loginId, String refreshToken) {
}
