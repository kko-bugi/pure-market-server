package com.kkobugi.puremarket.giveaway.presentation;

import com.kkobugi.puremarket.common.BaseException;
import com.kkobugi.puremarket.common.BaseResponse;
import com.kkobugi.puremarket.giveaway.application.GiveawayService;
import com.kkobugi.puremarket.giveaway.domain.dto.GiveawayPostRequest;
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
    public BaseResponse<?> getGiveawayList() {
        try {
            return new BaseResponse<>(giveawayService.getGiveawayList());
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 나눔글 상세 조회
    @GetMapping("/{giveawayIdx}")
    public BaseResponse<?> getGiveaway(@PathVariable Long giveawayIdx) {
        try {
            return new BaseResponse<>(giveawayService.getGiveawayPost(giveawayIdx));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 나눔글 등록
    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
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
    public BaseResponse<?> changeGiveawayStatus(@PathVariable Long giveawayIdx) {
        try {
            giveawayService.changeGiveawayStatus(giveawayIdx);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // [작성자] 나눔글 삭제
    @PatchMapping("")
    public BaseResponse<?> deleteGiveaway(@RequestParam Long giveawayIdx) {
        try {
            giveawayService.deleteGiveaway(giveawayIdx);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
