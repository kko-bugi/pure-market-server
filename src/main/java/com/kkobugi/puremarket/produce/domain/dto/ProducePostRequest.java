package com.kkobugi.puremarket.produce.domain.dto;

import org.springframework.web.multipart.MultipartFile;

public record ProducePostRequest(String title,
                                 String content,
                                 Integer price,
                                 MultipartFile produceImage) {
}
