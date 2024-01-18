package com.kkobugi.puremarket.user.domain.dto;

public record LoginRequest(
        String loginId,
        String password) {}