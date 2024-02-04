package com.kkobugi.puremarket.giveaway.application;

import com.kkobugi.puremarket.common.BaseException;
import com.kkobugi.puremarket.giveaway.domain.dto.GiveawayListResponse;
import com.kkobugi.puremarket.giveaway.repository.GiveawayRepository;
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
    public GiveawayListResponse getGiveawayList() throws BaseException {
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
}
