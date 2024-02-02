package com.kkobugi.puremarket.produce.application;

import com.kkobugi.puremarket.common.BaseException;
import com.kkobugi.puremarket.produce.domain.dto.ProduceListResponse;
import com.kkobugi.puremarket.produce.repository.ProduceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.kkobugi.puremarket.common.constants.Constant.ACTIVE;
import static com.kkobugi.puremarket.common.enums.BaseResponseStatus.DATABASE_ERROR;
import static com.kkobugi.puremarket.common.enums.BaseResponseStatus.NULL_PRODUCE_LIST;

@Service
@RequiredArgsConstructor
public class ProduceService {
    private final ProduceRepository produceRepository;

    // 목록 조회
    public ProduceListResponse getProduceList() throws BaseException {
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
}
