package com.kkobugi.puremarket.home.presentation;

import com.kkobugi.puremarket.common.BaseException;
import com.kkobugi.puremarket.common.BaseResponse;
import com.kkobugi.puremarket.home.application.HomeService;
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
    public BaseResponse<?> getHome() {
        try {
            return new BaseResponse<>(homeService.getHome());
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
