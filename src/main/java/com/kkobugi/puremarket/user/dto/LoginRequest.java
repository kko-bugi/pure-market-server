package com.kkobugi.puremarket.user.dto;

public record LoginRequest(
        String loginId,
        String password) {}