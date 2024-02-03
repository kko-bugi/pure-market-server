package com.kkobugi.puremarket.produce.domain.dto;

public record ProduceResponse(Long produceIdx,
                              String title,
                              String content,
                              Integer price,
                              String produceImage,
                              String produceStatus,
                              String nickname,
                              String contact,
                              String profileImage,
                              boolean isWriter) {}
