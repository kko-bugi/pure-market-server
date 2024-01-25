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
    INVALID_USER_IDX(false, 2000, "잘못된 user Idx입니다."),
    DUPLICATED_LOGIN_ID(false, 2001, "이미 사용중인 아이디입니다."),
    DUPLICATED_NICKNAME(false, 2002, "이미 사용중인 닉네임입니다."),
    UNMATCHED_PASSWORD(false, 2003, "비밀번호가 일치하지 않습니다."),
    INVALID_PASSWORD(false, 2004, "비민번호가 잘못되었습니다."),
    INVALID_LOGIN_ID(false, 2005, "잘못된 아이디입니다."),
    INVALID_TOKEN(false, 2006, "잘못된 JWT입니다."),
    INVALID_REFRESH_TOKEN(false, 2007, "잘못된 RefreshToken 입니다."),

    // produce(2100-2199)

    // recipe(2200-2299)

    // ingredient(2300-2399)

    // giveaway(2400-2499)


    /**
     * 3000: Response 오류
     */
    // users(3000~3099)

    // produce(3100-3199)

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
