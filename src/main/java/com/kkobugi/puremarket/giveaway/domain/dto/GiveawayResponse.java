package com.kkobugi.puremarket.giveaway.domain.dto;

import com.kkobugi.puremarket.comment.domain.dto.CommentDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "나눔글 상세 조회 응답")
public record GiveawayResponse(
        @Schema(description = "나눔글 Idx", example = "1")
        Long giveawayIdx,
        @Schema(description = "글 제목", example = "나눔글")
        String title,
        @Schema(description = "글 내용", example = "나눕니다.")
        String content,
        @Schema(description = "글 이미지 url", example = "https://dwffwdfdwbwv")
        String giveawayImage,
        @Schema(description = "나눔 상태", example = "나눔완료")
        String giveawayStatus,
        @Schema(description = "닉네임", example = "꼬부기")
        String nickname,
        @Schema(description = "연락처", example = "01012345678")
        String contact,
        @Schema(description = "프로필 이미지 url", example = "https://dwffwdfdwbwv")
        String profileImage,
        @Schema(description = "글 작성자 여부", example = "true")
        boolean isWriter,
        @Schema(description = "댓글 리스트")
        List<CommentDto> commentList) {}
