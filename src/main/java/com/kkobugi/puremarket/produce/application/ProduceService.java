package com.kkobugi.puremarket.produce.application;

import com.kkobugi.puremarket.common.BaseException;
import com.kkobugi.puremarket.produce.domain.dto.ProduceListResponse;
import com.kkobugi.puremarket.produce.domain.dto.ProduceResponse;
import com.kkobugi.puremarket.produce.domain.entity.Produce;
import com.kkobugi.puremarket.produce.repository.ProduceRepository;
import com.kkobugi.puremarket.user.application.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.kkobugi.puremarket.common.constants.Constant.ACTIVE;
import static com.kkobugi.puremarket.common.enums.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class ProduceService {
    private final ProduceRepository produceRepository;
    private final AuthService authService;

    // 판매글 목록 조회
    public ProduceListResponse getProduceList() throws BaseException { // TODO: 판매 상태에 따라 조회 수정 필요
        try {
            // 판매 상태인 글 최신순으로 조회
            List<ProduceListResponse.ProduceDto> produceList = produceRepository.findByStatusEqualsOrderByCreatedDateDesc(ACTIVE).stream()
                    .map(produce -> new ProduceListResponse.ProduceDto(
                            produce.getProduceIdx(),
                            produce.getTitle(),
                            produce.getPrice(),
                            produce.getProduceImage()))
                    .collect(Collectors.toList());
            if (produceList.isEmpty()) throw new BaseException(NULL_PRODUCE_LIST);
            return new ProduceListResponse(produceList);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 판매글 상세 조회
    public ProduceResponse getProducePost(Long produceIdx) throws BaseException {
        try {
            Long userIdx = authService.getUserIdxFromToken();
            Produce produce = produceRepository.findById(produceIdx).orElseThrow(() -> new BaseException(INVALID_PRODUCE_IDX));

            boolean isWriter = false;
            if (userIdx != null && produce.getUser() != null) {
                isWriter = userIdx.equals(produce.getUser().getUserIdx());
            }
            // TODO: 판매완료 글 상세 페이지 구현에 따라 status 로직 변경 필요
            return new ProduceResponse(produce.getProduceIdx(), produce.getTitle(), produce.getContent(), produce.getPrice(), produce.getProduceImage(), produce.getStatus(),
                                        produce.getUser().getNickname(), produce.getUser().getContact(), produce.getUser().getProfileImage(), isWriter);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}