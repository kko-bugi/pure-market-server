package com.kkobugi.puremarket.giveaway.application;

import com.kkobugi.puremarket.common.BaseException;
import com.kkobugi.puremarket.common.gcs.GCSService;
import com.kkobugi.puremarket.giveaway.domain.dto.*;
import com.kkobugi.puremarket.giveaway.domain.entity.Giveaway;
import com.kkobugi.puremarket.giveaway.repository.GiveawayRepository;
import com.kkobugi.puremarket.user.application.AuthService;
import com.kkobugi.puremarket.user.domain.entity.User;
import com.kkobugi.puremarket.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.kkobugi.puremarket.common.constants.Constant.Giveaway.DONE;
import static com.kkobugi.puremarket.common.constants.Constant.Giveaway.GIVEAWAY;
import static com.kkobugi.puremarket.common.constants.Constant.INACTIVE;
import static com.kkobugi.puremarket.common.enums.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class GiveawayService {
    private final GiveawayRepository giveawayRepository;
    private final AuthService authService;
    private final UserRepository userRepository;
    private final GCSService gcsService;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    // 나눔글 목록 조회
    public GiveawayListResponse getGiveawayList() throws BaseException {
        try {
            // 나눔중 상태인 글 최신순으로 조회
            List<GiveawayListResponse.GiveawayDto> giveawayList = giveawayRepository.findByStatusEqualsOrderByCreatedDateDesc(GIVEAWAY).stream()
                    .map(giveaway -> new GiveawayListResponse.GiveawayDto(
                            giveaway.getGiveawayIdx(),
                            giveaway.getTitle(),
                            giveaway.getContent(),
                            giveaway.getGiveawayImage(),
                            giveaway.getStatus())).toList();

            // 나눔완료 상태인 글 최신순으로 조회
            List<GiveawayListResponse.GiveawayDto> doneList = giveawayRepository.findByStatusEqualsOrderByCreatedDateDesc(DONE).stream()
                    .map(giveaway -> new GiveawayListResponse.GiveawayDto(
                            giveaway.getGiveawayIdx(),
                            giveaway.getTitle(),
                            giveaway.getContent(),
                            giveaway.getGiveawayImage(),
                            giveaway.getStatus())).toList();

            List<GiveawayListResponse.GiveawayDto> giveawayDtoList = new ArrayList<>();
            giveawayDtoList.addAll(giveawayList);
            giveawayDtoList.addAll(doneList);

            return new GiveawayListResponse(giveawayDtoList);
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 나눔글 상세 조회
    public GiveawayResponse getGiveawayPost(Long giveawayIdx) throws BaseException {
        try {
            Long userIdx = authService.getUserIdx();
            Giveaway giveaway = giveawayRepository.findById(giveawayIdx).orElseThrow(() -> new BaseException(INVALID_GIVEAWAY_IDX));
            if (giveaway.getStatus().equals(INACTIVE)) throw new BaseException(ALREADY_DELETED_GIVEAWAY);

            boolean isWriter = false;
            if (userIdx != null && giveaway.getUser() != null) {
                isWriter = userIdx.equals(giveaway.getUser().getUserIdx());
            }
            return new GiveawayResponse(giveaway.getGiveawayIdx(), giveaway.getTitle(), giveaway.getContent(), giveaway.getGiveawayImage(), giveaway.getStatus(),
                                            giveaway.getUser().getNickname(), giveaway.getUser().getContact(), giveaway.getUser().getProfileImage(), isWriter);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 나눔글 등록
    @Transactional(rollbackFor = Exception.class)
    public void postGiveaway(MultipartFile image, GiveawayPostRequest giveawayPostRequest) throws BaseException {
        try {
            Long userIdx = getUserIdxWithValidation();
            User writer = userRepository.findByUserIdx(userIdx).orElseThrow(() -> new BaseException(INVALID_USER_IDX));

            // upload image
            String fullPath = gcsService.uploadImage("giveaway", image);
            String giveawayImageUrl = "https://storage.googleapis.com/"+bucketName+"/"+fullPath;

            Giveaway giveaway = new Giveaway(writer, giveawayPostRequest.title(), giveawayPostRequest.content(), giveawayImageUrl);
            giveaway.setStatus(GIVEAWAY);

            giveawayRepository.save(giveaway);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 나눔 상태 변경
    @Transactional(rollbackFor = Exception.class)
    public void changeGiveawayStatus(Long giveawayIdx) throws BaseException {
        try {
            Long userIdx = getUserIdxWithValidation();
            User user = userRepository.findByUserIdx(userIdx).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            Giveaway giveaway = giveawayRepository.findById(giveawayIdx).orElseThrow(() -> new BaseException(INVALID_GIVEAWAY_IDX));

            validateWriter(user, giveaway);

            giveaway.changeStatus(giveaway.getStatus());
            giveawayRepository.save(giveaway);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    private static void validateWriter(User user, Giveaway giveaway) throws BaseException {
        if (!giveaway.getUser().equals(user)) throw new BaseException(NO_GIVEAWAY_WRITER);
        if (giveaway.getStatus().equals(INACTIVE)) throw new BaseException(ALREADY_DELETED_GIVEAWAY);
    }

    // 나눔글 삭제
    @Transactional(rollbackFor = Exception.class)
    public void deleteGiveaway(Long giveawayIdx) throws BaseException {
        try {
            Long userIdx = getUserIdxWithValidation();
            User user = userRepository.findByUserIdx(userIdx).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            Giveaway giveaway = giveawayRepository.findById(giveawayIdx).orElseThrow(() -> new BaseException(INVALID_GIVEAWAY_IDX));

            validateWriter(user, giveaway);

            giveaway.delete();
            boolean isDeleted = gcsService.deleteImage(giveaway.getGiveawayImage());
            if (!isDeleted) throw new BaseException(IMAGE_DELETE_FAIL);

            giveawayRepository.save(giveaway);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // [작성자] 나눔글 수정 화면 조회
    public GiveawayEditViewResponse getGiveawayEditView(Long giveawayIdx) throws BaseException {
        try {
            Giveaway giveaway = giveawayRepository.findById(giveawayIdx).orElseThrow(() -> new BaseException(INVALID_GIVEAWAY_IDX));
            if (giveaway.getStatus().equals(INACTIVE)) throw new BaseException(ALREADY_DELETED_GIVEAWAY);

            User user = userRepository.findByUserIdx(getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            validateWriter(user, giveaway);

            return new GiveawayEditViewResponse(giveaway.getTitle(), giveaway.getContent(), giveaway.getGiveawayImage(),
                    giveaway.getUser().getNickname(), giveaway.getUser().getContact(), giveaway.getUser().getProfileImage());
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // [작성자] 나눔글 수정
    @Transactional(rollbackFor = Exception.class)
    public void editGiveaway(Long giveawayIdx, MultipartFile image, GiveawayEditRequest giveawayEditRequest) throws BaseException {
        try {
            Giveaway giveaway = giveawayRepository.findById(giveawayIdx).orElseThrow(() -> new BaseException(INVALID_GIVEAWAY_IDX));
            if (giveaway.getStatus().equals(INACTIVE)) throw new BaseException(ALREADY_DELETED_GIVEAWAY);

            User user = userRepository.findByUserIdx(getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            validateWriter(user, giveaway);

            if (giveawayEditRequest.title() != null) {
                if (!giveawayEditRequest.title().equals("") && !giveawayEditRequest.title().equals(" "))
                    giveaway.modifyTitle(giveawayEditRequest.title());
                else throw new BaseException(BLANK_GIVEAWAY_TITLE);
            }
            if (giveawayEditRequest.content() != null) {
                if (!giveawayEditRequest.content().equals("") && !giveawayEditRequest.content().equals(" "))
                    giveaway.modifyContent(giveawayEditRequest.content());
                else throw new BaseException(BLANK_GIVEAWAY_CONTENT);
            }
            if (image != null) {
                // delete previous image
                boolean isDeleted = gcsService.deleteImage(giveaway.getGiveawayImage());
                if (!isDeleted) throw new BaseException(IMAGE_DELETE_FAIL);

                // upload new image
                String fullPath = gcsService.uploadImage("giveaway", image);
                String newImageUrl = "https://storage.googleapis.com/"+bucketName+"/"+fullPath;
                giveaway.modifyImage(newImageUrl);
            } else throw new BaseException(NULL_GIVEAWAY_IMAGE);
            giveawayRepository.save(giveaway);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 비회원 예외처리
    private Long getUserIdxWithValidation() throws BaseException {
        Long userIdx = authService.getUserIdx();
        if (userIdx == null) throw new BaseException(NULL_ACCESS_TOKEN);
        return userIdx;
    }
}
