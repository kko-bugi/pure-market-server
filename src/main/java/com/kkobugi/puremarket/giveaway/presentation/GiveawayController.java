package com.kkobugi.puremarket.giveaway.presentation;

import com.kkobugi.puremarket.common.BaseException;
import com.kkobugi.puremarket.common.BaseResponse;
import com.kkobugi.puremarket.giveaway.application.GiveawayService;
import com.kkobugi.puremarket.produce.domain.dto.GiveawayPostRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public BaseResponse<?> getGiveawayList(@PathVariable Long giveawayIdx) {
        try {
            return new BaseResponse<>(giveawayService.getGiveawayPost(giveawayIdx));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 나눔글 등록
    @PostMapping("")
    public BaseResponse<?> postGiveaway(GiveawayPostRequest giveawayPostRequest) {
        try {
            giveawayService.postGiveaway(giveawayPostRequest);
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
    public BaseResponse<?> deleteProduce(@RequestParam Long giveawayIdx) {
        try {
            giveawayService.deleteGiveaway(giveawayIdx);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
