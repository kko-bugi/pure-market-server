package com.kkobugi.puremarket.home.presentation;

import com.kkobugi.puremarket.common.BaseException;
import com.kkobugi.puremarket.common.BaseResponse;
import com.kkobugi.puremarket.home.application.HomeService;
import com.kkobugi.puremarket.home.domain.dto.HomeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.kkobugi.puremarket.common.constants.RequestURI.home;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer")
@RequestMapping(home)
@Tag(name = "Home", description = "홈 API")
public class HomeController {

    private final HomeService homeService;

    @GetMapping("")
    @Operation(summary = "홈화면 조회", description = "홈화면을 조회한다. 글 목록이 비었을 시 예외처리 없이 빈 리스트를 반환한다.")
    @ApiResponses(value = {@ApiResponse(responseCode = "1000", description = "조회 성공")})
    public BaseResponse<HomeResponse> getHome() {
        try {
            return new BaseResponse<>(homeService.getHome());
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
