package com.kkobugi.puremarket.comment.domain.dto;

import java.time.LocalDate;

public record CommentDto(String writer,
                         String profileImage,
                         String content,
                         LocalDate createdDate) {}
