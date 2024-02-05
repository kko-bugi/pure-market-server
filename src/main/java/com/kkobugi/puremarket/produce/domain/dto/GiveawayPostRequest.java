package com.kkobugi.puremarket.produce.domain.dto;

import org.springframework.web.multipart.MultipartFile;

public record GiveawayPostRequest(String title,
                                  String content,
                                  MultipartFile giveawayImage) {}
