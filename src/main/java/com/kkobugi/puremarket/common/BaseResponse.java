package com.kkobugi.puremarket.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.kkobugi.puremarket.common.enums.BaseResponseStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.kkobugi.puremarket.common.enums.BaseResponseStatus.SUCCESS;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
@Schema(description = "베이스 응답")
public class BaseResponse<T> {

    @JsonProperty("isSuccess")
    @Schema(description = "응답 성공 여부", example = "true")
    private final Boolean isSuccess;

    @Schema(description = "응답 메시지", example = "요청에 성공하였습니다.")
    private final String message;

    @Schema(description = "응답 코드", example = "1000")
    private final int code;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "응답 결과")
    private T result;

    public BaseResponse(T result) {
        this.isSuccess = SUCCESS.isSuccess();
        this.message = SUCCESS.getMessage();
        this.code = SUCCESS.getCode();
        this.result = result;
    }

    public BaseResponse(BaseResponseStatus status) {
        this.isSuccess = status.isSuccess();
        this.message = status.getMessage();
        this.code = status.getCode();
    }

    // 요청은 성공했지만 데이터에 이상이 있는 경우
    public BaseResponse(T result, BaseResponseStatus status) {
        this.isSuccess = status.isSuccess();
        this.message = status.getMessage();
        this.code = status.getCode();
        this.result = result;
    }
}