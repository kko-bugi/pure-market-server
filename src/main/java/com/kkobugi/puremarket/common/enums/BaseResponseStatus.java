package com.kkobugi.puremarket.common.enums;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {
    /**
     * 1000: 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),

    /**
     * 2000: Request 오류
     */
    // users(2000-2099)
    DUPLICATED_LOGIN_ID(false, 2000, "이미 사용중인 아이디입니다."),
    DUPLICATED_NICKNAME(false, 2001, "이미 사용중인 닉네임입니다."),
    UNMATCHED_PASSWORD(false, 2002, "비밀번호가 일치하지 않습니다."),
    INVALID_PASSWORD(false, 2003, "비밀번호가 잘못되었습니다."),
    INVALID_LOGIN_ID(false, 2004, "잘못된 아이디입니다."),
    INVALID_ACCESS_TOKEN(false, 2005, "잘못된 AccessToken 입니다."),
    INVALID_REFRESH_TOKEN(false, 2006, "잘못된 RefreshToken 입니다."),
    NULL_ACCESS_TOKEN(false, 2007, "AccessToken을 입력해주세요."),

    // produce(2100-2199)
    INVALID_PRODUCE_IDX(false, 2100, "잘못된 produce idx입니다."),

    // recipe(2200-2299)

    // ingredient(2300-2399)

    // giveaway(2400-2499)


    /**
     * 3000: Response 오류
     */
    // users(3000~3099)
    INVALID_USER_IDX(false, 3000, "해당 user를 찾을 수 없습니다."),

    // produce(3100-3199)
    NULL_PRODUCE_LIST(false, 3100, "판매글 목록이 비었습니다."),

    // recipe(3200-3299)

    // ingredient(3300-3399)

    // giveaway(3400-3499)


    /**
     * 4000: DB, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패했습니다.");

    private final boolean isSuccess;
    private final int code;
    private final String message;

    BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
