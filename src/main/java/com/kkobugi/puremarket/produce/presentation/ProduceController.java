package com.kkobugi.puremarket.produce.presentation;

import com.kkobugi.puremarket.common.BaseException;
import com.kkobugi.puremarket.common.BaseResponse;
import com.kkobugi.puremarket.produce.application.ProduceService;
import com.kkobugi.puremarket.produce.domain.dto.ProducePostRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.kkobugi.puremarket.common.constants.RequestURI.produce;
import static com.kkobugi.puremarket.common.enums.BaseResponseStatus.SUCCESS;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer")
@RequestMapping(produce)
@Tag(name = "Produce", description = "판매글 API")
public class ProduceController {

    private final ProduceService produceService;

    // 판매글 목록 조회
    @GetMapping("")
    @Operation(summary = "판매글 목록 조회", description = "판매글 전체 목록을 조회한다. 글 목록이 비었을 시 예외처리 없이 빈 리스트를 반환한다.")
    @ApiResponses(value = {@ApiResponse(responseCode = "1000", description = "조회 성공")})
    public BaseResponse<?> getProduceList() {
        try {
            return new BaseResponse<>(produceService.getProduceList());
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 판매글 상세 조회
    @GetMapping("/{produceIdx}")
    @Operation(summary = "판매글 상세 조회", description = "판매글의 상세 페이지를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "조회 성공"),
            @ApiResponse(responseCode = "2008", description = "빈 access token", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "2100", description = "잘못된 판매글 Idx", content = @Content(schema = @Schema(implementation = BaseResponse.class)))})
    public BaseResponse<?> getProduce(@Parameter(description = "판매글 Idx", in = ParameterIn.PATH) @PathVariable Long produceIdx) {
        try {
            return new BaseResponse<>(produceService.getProducePost(produceIdx));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 판매글 등록
    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "판매글 등록", description = "판매글을 작성한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "등록 성공"),
            @ApiResponse(responseCode = "2000", description = "잘못된 userIdx", content = @Content(schema = @Schema(implementation = BaseResponse.class)))})
    public BaseResponse<?> postProduce(@RequestPart(value = "image", required = false) MultipartFile image, @RequestPart(value = "produceRequest") ProducePostRequest producePostRequest) {
        try {
            produceService.postProduce(image, producePostRequest);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // [작성자] 판매 상태 변경
    @PatchMapping("/{produceIdx}")
    @Operation(summary = "판매 상태 변경", description = "판매글 상태를 변경한다.(판매중/판매완료)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "변경 성공"),
            @ApiResponse(responseCode = "2000", description = "잘못된 userIdx", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "2100", description = "잘못된 판매글 Idx", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "2102", description = "판매글 작성자가 아닙니다.", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "2103", description = "이미 삭제된 판매글", content = @Content(schema = @Schema(implementation = BaseResponse.class)))})
    public BaseResponse<?> changeProduceStatus(@Parameter(description = "판매글 Idx", in = ParameterIn.PATH) @PathVariable Long produceIdx) {
        try {
            produceService.changeProduceStatus(produceIdx);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // [작성자] 판매글 삭제
    @PatchMapping("")
    @Operation(summary = "판매글 삭제", description = "판매글을 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "삭제 성공"),
            @ApiResponse(responseCode = "2000", description = "잘못된 userIdx", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "2100", description = "잘못된 판매글 Idx", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "2102", description = "판매글 작성자가 아닙니다.", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "2103", description = "이미 삭제된 판매글", content = @Content(schema = @Schema(implementation = BaseResponse.class)))})
    public BaseResponse<?> deleteProduce(@Parameter(description = "판매글 Idx", in = ParameterIn.QUERY) @RequestParam Long produceIdx) {
        try {
            produceService.deleteProduce(produceIdx);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
