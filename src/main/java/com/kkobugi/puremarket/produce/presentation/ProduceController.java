package com.kkobugi.puremarket.produce.presentation;

import com.kkobugi.puremarket.common.BaseException;
import com.kkobugi.puremarket.common.BaseResponse;
import com.kkobugi.puremarket.produce.application.ProduceService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.kkobugi.puremarket.common.constants.RequestURI.produce;


@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer")
@RequestMapping(produce)
@Tag(name = "Produce", description = "판매글 API")
public class ProduceController {

    private final ProduceService produceService;

    // 판매글 목록 조회
    @GetMapping("")
    public BaseResponse<?> getProduceList() {
        try {
            return new BaseResponse<>(produceService.getProduceList());
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
