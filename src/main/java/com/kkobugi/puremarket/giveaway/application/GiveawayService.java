package com.kkobugi.puremarket.giveaway.application;

import com.kkobugi.puremarket.common.BaseException;
import com.kkobugi.puremarket.giveaway.domain.dto.GiveawayListResponse;
import com.kkobugi.puremarket.giveaway.domain.dto.GiveawayResponse;
import com.kkobugi.puremarket.giveaway.domain.entity.Giveaway;
import com.kkobugi.puremarket.giveaway.repository.GiveawayRepository;
import com.kkobugi.puremarket.user.application.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.kkobugi.puremarket.common.constants.Constant.Giveaway.GIVEAWAY;
import static com.kkobugi.puremarket.common.enums.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class GiveawayService {
    private final GiveawayRepository giveawayRepository;
    private final AuthService authService;

    // 나눔글 목록 조회
    public GiveawayListResponse getGiveawayList() throws BaseException { // TODO: 나눔완료 글도 목록 조회 시 포함하도록 수정 팔요
        try {
            List<GiveawayListResponse.GiveawayDto> giveawayList = giveawayRepository.findByStatusEqualsOrderByCreatedDateDesc(GIVEAWAY).stream()
                    .map(giveaway -> new GiveawayListResponse.GiveawayDto(
                            giveaway.getGiveawayIdx(),
                            giveaway.getTitle(),
                            giveaway.getContent(),
                            giveaway.getGiveawayImage(),
                            giveaway.getStatus()))
                    .collect(Collectors.toList());
            if (giveawayList.isEmpty()) throw new BaseException(NULL_GIVEAWAY_LIST);
            return new GiveawayListResponse(giveawayList);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 나눔글 상세 조회
    public GiveawayResponse getGiveawayPost(Long giveawayIdx) throws BaseException {
        try {
            Long userIdx = authService.getUserIdxFromToken();
            Giveaway giveaway = giveawayRepository.findById(giveawayIdx).orElseThrow(() -> new BaseException(INVALID_GIVEAWAY_IDX));

            boolean isWriter = false;
            if (userIdx != null && giveaway.getUser() != null) {
                isWriter = userIdx.equals(giveaway.getUser().getUserIdx());
            }
            // TODO: 나눔완료 글 상세 페이지 구현에 따라 status 로직 변경 필요
            return new GiveawayResponse(giveaway.getGiveawayIdx(), giveaway.getTitle(), giveaway.getContent(), giveaway.getGiveawayImage(), giveaway.getStatus(),
                                            giveaway.getUser().getNickname(), giveaway.getUser().getContact(), giveaway.getUser().getProfileImage(), isWriter);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
