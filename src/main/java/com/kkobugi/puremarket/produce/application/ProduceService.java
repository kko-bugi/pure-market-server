package com.kkobugi.puremarket.produce.application;

import com.kkobugi.puremarket.common.BaseException;
import com.kkobugi.puremarket.common.gcs.GCSService;
import com.kkobugi.puremarket.produce.domain.dto.ProduceListResponse;
import com.kkobugi.puremarket.produce.domain.dto.ProducePostRequest;
import com.kkobugi.puremarket.produce.domain.dto.ProduceResponse;
import com.kkobugi.puremarket.produce.domain.entity.Produce;
import com.kkobugi.puremarket.produce.repository.ProduceRepository;
import com.kkobugi.puremarket.user.application.AuthService;
import com.kkobugi.puremarket.user.domain.entity.User;
import com.kkobugi.puremarket.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.kkobugi.puremarket.common.constants.Constant.INACTIVE;
import static com.kkobugi.puremarket.common.constants.Constant.Produces.FOR_SALE;
import static com.kkobugi.puremarket.common.enums.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class ProduceService {
    private final ProduceRepository produceRepository;
    private final AuthService authService;
    private final GCSService gcsService;
    private final UserRepository userRepository;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    // 판매글 목록 조회
    public ProduceListResponse getProduceList() throws BaseException { // TODO: status=SOLD_OUT인 것도 조회
        try {
            // 판매 상태인 글 최신순으로 조회
            List<ProduceListResponse.ProduceDto> produceList = produceRepository.findByStatusEqualsOrderByCreatedDateDesc(FOR_SALE).stream()
                    .map(produce -> new ProduceListResponse.ProduceDto(
                            produce.getProduceIdx(),
                            produce.getTitle(),
                            produce.getPrice(),
                            produce.getProduceImage(),
                            produce.getStatus()))
                    .collect(Collectors.toList());
            if (produceList.isEmpty()) throw new BaseException(NULL_PRODUCE_LIST);
            return new ProduceListResponse(produceList);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 판매글 상세 조회
    public ProduceResponse getProducePost(Long produceIdx) throws BaseException {
        try {
            Long userIdx = authService.getUserIdxFromToken();
            Produce produce = produceRepository.findById(produceIdx).orElseThrow(() -> new BaseException(INVALID_PRODUCE_IDX));

            boolean isWriter = false;
            if (userIdx != null && produce.getUser() != null) {
                isWriter = userIdx.equals(produce.getUser().getUserIdx());
            }
            // TODO: 판매완료 글 상세 페이지 구현에 따라 status 로직 변경 필요
            return new ProduceResponse(produce.getProduceIdx(), produce.getTitle(), produce.getContent(), produce.getPrice(), produce.getProduceImage(), produce.getStatus(),
                                        produce.getUser().getNickname(), produce.getUser().getContact(), produce.getUser().getProfileImage(), isWriter);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 판매글 등록
    @Transactional(rollbackFor = Exception.class)
    public void postProduce(ProducePostRequest producePostRequest) throws BaseException {
        try {
            User writer = userRepository.findByUserIdx(authService.getUserIdxFromToken()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));

            // validation
            if (producePostRequest.title().length() > 32) throw new BaseException(TITLE_EXCEEDED_MAX_LIMIT);

            // upload image
            String fullPath = gcsService.uploadImage("produce", producePostRequest.produceImage());
            String produceImageUrl = "https://storage.googleapis.com/"+bucketName+"/"+fullPath;

            Produce produce = new Produce(writer, producePostRequest.title(), producePostRequest.content(), producePostRequest.price(), produceImageUrl);
            produce.setStatus(FOR_SALE);
            produceRepository.save(produce);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 판매 상태 변경
    public void changeProduceStatus(Long produceIdx) throws BaseException {
        try {
            User user = userRepository.findByUserIdx(authService.getUserIdxFromToken()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            Produce produce = produceRepository.findById(produceIdx).orElseThrow(() -> new BaseException(INVALID_PRODUCE_IDX));

            // validation
            if (!produce.getUser().equals(user)) throw new BaseException(NO_PRODUCE_WRITER);
            if (produce.getStatus().equals(INACTIVE)) throw new BaseException(ALREADY_DELETED_PRODUCE);

            produce.changeStatus(produce.getStatus());
            produceRepository.save(produce);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
