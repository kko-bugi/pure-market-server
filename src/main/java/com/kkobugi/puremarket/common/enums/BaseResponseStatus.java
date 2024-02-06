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
    INVALID_REFRESH_TOKEN(false, 2006, "잘못된 Refresh Token 입니다."),
    NULL_ACCESS_TOKEN(false, 2007, "Access Token을 입력해주세요."),

    // produce(2100-2199)
    INVALID_PRODUCE_IDX(false, 2100, "잘못된 판매글 idx 입니다."),
    TITLE_EXCEEDED_MAX_LIMIT(false, 2101, "제목은 32자 이하여야 합니다."),
    NO_PRODUCE_WRITER(false, 2102, "해당 판매글의 작성자가 아닙니다."),
    ALREADY_DELETED_PRODUCE(false, 2103, "이미 삭제된 판매글입니다."),

    // recipe(2200-2299)
    INVALID_RECIPE_IDX(false, 2200, "잘못된 레시피글 idx 입니다."),
    NO_RECIPE_WRITER(false, 2201, "해당 레시피글의 작성자가 아닙니다."),
    ALREADY_DELETED_RECIPE(false, 2202, "이미 삭제된 레시피글입니다."),

    // ingredient(2300-2399)

    // giveaway(2400-2499)
    INVALID_GIVEAWAY_IDX(false, 2400, "잘못된 나눔글 idx 입니다."),
    NO_GIVEAWAY_WRITER(false, 2401, "해당 나눔글의 작성자가 아닙니다."),
    ALREADY_DELETED_GIVEAWAY(false, 2402, "이미 삭제된 나눔글입니다."),


    /**
     * 3000: Response 오류
     */
    // users(3000~3099)
    INVALID_USER_IDX(false, 3000, "user idx가 잘못 되었습니다."),
    NO_MATCH_USER(false, 3001, "해당 user를 찾을 수 없습니다."),

    // produce(3100-3199)
    NULL_PRODUCE_LIST(false, 3100, "판매글 목록이 비었습니다."),

    // recipe(3200-3299)

    // ingredient(3300-3399)

    // giveaway(3400-3499)
    NULL_GIVEAWAY_LIST(false, 3400, "나눔글 목록이 비었습니다."),


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
