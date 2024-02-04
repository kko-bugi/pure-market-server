package com.kkobugi.puremarket.giveaway.presentation;

import com.kkobugi.puremarket.common.BaseException;
import com.kkobugi.puremarket.common.BaseResponse;
import com.kkobugi.puremarket.giveaway.application.GiveawayService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.kkobugi.puremarket.common.constants.RequestURI.giveaway;

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
}
