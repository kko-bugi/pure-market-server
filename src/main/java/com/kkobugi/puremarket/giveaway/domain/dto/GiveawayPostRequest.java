package com.kkobugi.puremarket.giveaway.domain.dto;

import org.springframework.web.multipart.MultipartFile;

public record GiveawayPostRequest(String title,
                                  String content,
                                  MultipartFile giveawayImage) {}
