package com.kkobugi.puremarket.user.application;

import com.kkobugi.puremarket.common.BaseException;
import com.kkobugi.puremarket.common.gcs.GCSService;
import com.kkobugi.puremarket.giveaway.repository.GiveawayRepository;
import com.kkobugi.puremarket.produce.repository.ProduceRepository;
import com.kkobugi.puremarket.recipe.repository.RecipeRepository;
import com.kkobugi.puremarket.user.domain.dto.*;
import com.kkobugi.puremarket.user.domain.entity.User;
import com.kkobugi.puremarket.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.kkobugi.puremarket.common.constants.Constant.ACTIVE;
import static com.kkobugi.puremarket.common.constants.Constant.Giveaway.DONE;
import static com.kkobugi.puremarket.common.constants.Constant.Giveaway.GIVEAWAY;
import static com.kkobugi.puremarket.common.constants.Constant.Produce.FOR_SALE;
import static com.kkobugi.puremarket.common.constants.Constant.Produce.SOLD_OUT;
import static com.kkobugi.puremarket.common.enums.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final BCryptPasswordEncoder encoder;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final ProduceRepository produceRepository;
    private final RecipeRepository recipeRepository;
    private final GiveawayRepository giveawayRepository;
    private final GCSService gcsService;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    // 회원가입
    @Transactional(rollbackFor = Exception.class)
    public JwtDto signup(MultipartFile image, SignupRequest signupRequest) throws BaseException {
        try {
            if(!signupRequest.password().equals(signupRequest.passwordCheck())) throw new BaseException(UNMATCHED_PASSWORD);

            String fullPath = gcsService.uploadImage("users", image); // 프로필 이미지 업로드
            String profileImageUrl = "https://storage.googleapis.com/"+bucketName+"/"+fullPath;

            User newUser = signupRequest.toUser(encoder.encode(signupRequest.password()), profileImageUrl);
            userRepository.save(newUser);
            return authService.generateToken(newUser);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 아이디 중복 체크
    public void validateLoginId(String loginId) throws BaseException {
        if(userRepository.existsByLoginId(loginId)) throw new BaseException(DUPLICATED_LOGIN_ID);
    }

    // 닉네임 중복 체크
    public void validateNickname(String nickname) throws BaseException {
        if(userRepository.existsByNickname(nickname)) throw new BaseException(DUPLICATED_NICKNAME);
    }

    // 로그인
    @Transactional(rollbackFor = Exception.class)
    public LoginResponse login(LoginRequest loginRequest) throws BaseException {
        try {
            User user = userRepository.findByLoginId(loginRequest.loginId()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            if(!encoder.matches(loginRequest.password(), user.getPassword())) throw new BaseException(INVALID_PASSWORD);

            JwtDto jwtDto = authService.generateToken(user); 
            user.login(); // active
            userRepository.save(user);

            return new LoginResponse(jwtDto.accessToken(), jwtDto.refreshToken());
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public User getUserByUserIdx(Long userIdx) {
        if(userIdx == null) return null;
        else {
            Optional<User> user = userRepository.findByUserIdx(userIdx);
            return user.orElse(null);
        }
    }

    // 로그아웃
    @Transactional(rollbackFor = Exception.class)
    public void logout(Long userIdx, String refreshToken ) throws BaseException {
        try {
            User user = userRepository.findByUserIdxAndStatusEquals(userIdx, ACTIVE).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            authService.logout(refreshToken);
            user.logout(); // LOGOUT
            userRepository.save(user);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // Access Token 재발급
    @Transactional(rollbackFor = Exception.class)
    public TokenResponse reissueAccessToken(ReissueTokenRequest reissueTokenRequest) throws BaseException {
        try {
            String loginId = authService.getLoginIdFromRedis(reissueTokenRequest.refreshToken());
            System.out.println(loginId);
            User user = userRepository.findByLoginIdAndStatusEquals(loginId, ACTIVE).orElseThrow(() -> new BaseException(NO_MATCH_USER));

            authService.validateRefreshToken(reissueTokenRequest);

            // refresh token이 만료되지 않은 경우 access token 재발급
            if (authService.checkExistsRedis(reissueTokenRequest.refreshToken())) {
                return new TokenResponse(authService.generateAccessToken(user));
            } else throw new BaseException(INVALID_REFRESH_TOKEN);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 회원 탈퇴
    @Transactional(rollbackFor = Exception.class)
    public void signOut(Long userIdx, String refreshToken) throws BaseException {
        try {
            User user = userRepository.findByUserIdxAndStatusEquals(userIdx, ACTIVE).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            authService.signOut(refreshToken);
            user.signout(); // INACTIVE
            userRepository.save(user);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 마이페이지 조회
    public MyPageResponse getMyPage() throws BaseException {
        try {
            Long userIdx = getUserIdxWithValidation();
            User user = userRepository.findByUserIdxAndStatusEquals(userIdx, ACTIVE).orElseThrow(() -> new BaseException(INVALID_USER_IDX));

            List<String> produceStatuses = Arrays.asList(FOR_SALE, SOLD_OUT);
            List<MyPageResponse.Produce> produceList = produceRepository.findTop4ByUserAndStatusInOrderByCreatedDateDesc(user, produceStatuses)
                    .stream()
                    .map(produce -> new MyPageResponse.Produce(
                            produce.getProduceIdx(),
                            produce.getTitle(),
                            produce.getProduceImage(),
                            produce.getCreatedDate(),
                            produce.getStatus()))
                    .collect(Collectors.toList());

            List<MyPageResponse.Recipe> recipeList = recipeRepository.findTop4ByUserAndStatusEqualsOrderByCreatedDateDesc(user, ACTIVE)
                    .stream()
                    .map(recipe -> new MyPageResponse.Recipe(
                            recipe.getRecipeIdx(),
                            recipe.getTitle(),
                            recipe.getRecipeImage(),
                            recipe.getCreatedDate()))
                    .collect(Collectors.toList());

            List<String> giveawayStatuses = Arrays.asList(GIVEAWAY, DONE);
            List<MyPageResponse.Giveaway> giveawayList = giveawayRepository.findTop4ByUserAndStatusInOrderByCreatedDateDesc(user, giveawayStatuses)
                    .stream()
                    .map(giveaway -> new MyPageResponse.Giveaway(
                            giveaway.getGiveawayIdx(),
                            giveaway.getTitle(),
                            giveaway.getGiveawayImage(),
                            giveaway.getCreatedDate(),
                            giveaway.getStatus()))
                    .collect(Collectors.toList());

            return new MyPageResponse(user.getNickname(), user.getProfileImage(), produceList, recipeList, giveawayList);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 유저 프로필 조회
    public ProfileResponse getProfile() throws BaseException {
        try {
            Long userIdx = authService.getUserIdx();
            User user = userRepository.findByUserIdxAndStatusEquals(userIdx, ACTIVE).orElseThrow(() -> new BaseException(INVALID_USER_IDX));

            return new ProfileResponse(user.getNickname(), user.getContact(), user.getProfileImage());
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
