package com.kkobugi.puremarket.giveaway.presentation;

import com.kkobugi.puremarket.common.BaseException;
import com.kkobugi.puremarket.common.BaseResponse;
import com.kkobugi.puremarket.giveaway.application.GiveawayService;
import com.kkobugi.puremarket.giveaway.domain.dto.GiveawayEditViewResponse;
import com.kkobugi.puremarket.giveaway.domain.dto.GiveawayListResponse;
import com.kkobugi.puremarket.giveaway.domain.dto.GiveawayPostRequest;
import com.kkobugi.puremarket.giveaway.domain.dto.GiveawayResponse;
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

import static com.kkobugi.puremarket.common.constants.RequestURI.giveaway;
import static com.kkobugi.puremarket.common.enums.BaseResponseStatus.SUCCESS;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer")
@RequestMapping(giveaway)
@Tag(name = "Giveaway", description = "나눔글 API")
public class GiveawayController {

    private final GiveawayService giveawayService;

    // 나눔글 목록 조회
    @GetMapping("")
    @Operation(summary = "나눔글 목록 조회", description = "나눔글 전체 목록을 조회한다. 글 목록이 비었을 시 예외처리 없이 빈 리스트를 반환한다.")
    @ApiResponses(value = {@ApiResponse(responseCode = "1000", description = "조회 성공")})
    public BaseResponse<GiveawayListResponse> getGiveawayList() {
        try {
            return new BaseResponse<>(giveawayService.getGiveawayList());
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 나눔글 상세 조회
    @GetMapping("/{giveawayIdx}")
    @Operation(summary = "나눔글 상세 조회", description = "나눔글의 상세 페이지를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "조회 성공"),
            @ApiResponse(responseCode = "2008", description = "빈 access token", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "2400", description = "잘못된 나눔글 Idx", content = @Content(schema = @Schema(implementation = BaseResponse.class)))})
    public BaseResponse<GiveawayResponse> getGiveaway(@Parameter(description = "나눔글 Idx", in = ParameterIn.PATH) @PathVariable Long giveawayIdx) {
        try {
            return new BaseResponse<>(giveawayService.getGiveawayPost(giveawayIdx));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 나눔글 등록
    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "나눔글 등록", description = "나눔글을 작성한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "등록 성공"),
            @ApiResponse(responseCode = "2000", description = "잘못된 userIdx", content = @Content(schema = @Schema(implementation = BaseResponse.class)))})
    public BaseResponse<?> postGiveaway(@RequestPart(value = "image", required = false) MultipartFile image, @RequestPart(value = "giveawayRequest") GiveawayPostRequest giveawayPostRequest) {
        try {
            giveawayService.postGiveaway(image, giveawayPostRequest);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // [작성자] 나눔 상태 변경
    @PatchMapping("/{giveawayIdx}")
    @Operation(summary = "나눔 상태 변경", description = "나눔글 상태를 변경한다.(나눔중/나눔완료)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "변경 성공"),
            @ApiResponse(responseCode = "2000", description = "잘못된 userIdx", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "2400", description = "잘못된 나눔글 Idx", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "2401", description = "나눔글 작성자가 아닙니다.", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "2402", description = "이미 삭제된 나눔글", content = @Content(schema = @Schema(implementation = BaseResponse.class)))})
    public BaseResponse<?> changeGiveawayStatus(@Parameter(description = "나눔글 Idx", in = ParameterIn.PATH) @PathVariable Long giveawayIdx) {
        try {
            giveawayService.changeGiveawayStatus(giveawayIdx);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // [작성자] 나눔글 삭제
    @PatchMapping("")
    @Operation(summary = "나눔글 삭제", description = "나눔글을 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "삭제 성공"),
            @ApiResponse(responseCode = "2000", description = "잘못된 userIdx", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "2400", description = "잘못된 나눔글 Idx", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "2401", description = "나눔글 작성자가 아닙니다.", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "2402", description = "이미 삭제된 나눔글", content = @Content(schema = @Schema(implementation = BaseResponse.class)))})
    public BaseResponse<?> deleteGiveaway(@Parameter(description = "나눔글 Idx", in = ParameterIn.QUERY) @RequestParam Long giveawayIdx) {
        try {
            giveawayService.deleteGiveaway(giveawayIdx);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // [작성자] 나눔글 수정화면 조회
    @GetMapping("/editView/{giveawayIdx}")
    @Operation(summary = "나눔글 수정화면 조회", description = "해당 나눔글의 원데이터를 조회한다.")
    public BaseResponse<GiveawayEditViewResponse> getGiveawayEditView(@Parameter(description = "나눔글 Idx", in = ParameterIn.PATH) @PathVariable Long giveawayIdx) {
        try {
            return new BaseResponse<>(giveawayService.getGiveawayEditView(giveawayIdx));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
