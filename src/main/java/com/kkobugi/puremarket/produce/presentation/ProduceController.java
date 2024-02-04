package com.kkobugi.puremarket.produce.presentation;

import com.kkobugi.puremarket.common.BaseException;
import com.kkobugi.puremarket.common.BaseResponse;
import com.kkobugi.puremarket.produce.application.ProduceService;
import com.kkobugi.puremarket.produce.domain.dto.ProducePostRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public BaseResponse<?> getProduceList() {
        try {
            return new BaseResponse<>(produceService.getProduceList());
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 판매글 상세 조회
    @GetMapping("/{produceIdx}")
    public BaseResponse<?> getProduceList(@PathVariable Long produceIdx) {
        try {
            return new BaseResponse<>(produceService.getProducePost(produceIdx));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 판매글 등록
    @PostMapping("")
    public BaseResponse<?> postProduce(ProducePostRequest producePostRequest) {
        try {
            produceService.postProduce(producePostRequest);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // [작성자] 판매 상태 변경
    @PatchMapping("/{produceIdx}")
    public BaseResponse<?> changeProduceStatus(@PathVariable Long produceIdx) {
        try {
            produceService.changeProduceStatus(produceIdx);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // [작성자] 판매글 삭제
    @PatchMapping("")
    public BaseResponse<?> deleteProduce(@RequestParam Long produceIdx) {
        try {
            produceService.deleteProduce(produceIdx);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
