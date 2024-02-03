package com.kkobugi.puremarket.produce.domain.dto;

import java.util.List;

public record ProduceListResponse(List<ProduceDto> produceList) {
    public record ProduceDto(
            Long produceIdx,
            String title,
            Integer price,
            String produceImage,
            String produceStatus) {
    }
}
