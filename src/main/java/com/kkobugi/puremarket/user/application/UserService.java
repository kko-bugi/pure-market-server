package com.kkobugi.puremarket.user.application;

import com.kkobugi.puremarket.common.BaseException;
import com.kkobugi.puremarket.giveaway.repository.GiveawayRepository;
import com.kkobugi.puremarket.produce.repository.ProduceRepository;
import com.kkobugi.puremarket.recipe.repository.RecipeRepository;
import com.kkobugi.puremarket.user.domain.dto.*;
import com.kkobugi.puremarket.user.domain.entity.User;
import com.kkobugi.puremarket.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.kkobugi.puremarket.common.constants.Constant.ACTIVE;
import static com.kkobugi.puremarket.common.enums.BaseResponseStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final BCryptPasswordEncoder encoder;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final ProduceRepository produceRepository;
    private final RecipeRepository recipeRepository;
    private final GiveawayRepository giveawayRepository;

    // 회원가입
    @Transactional(rollbackFor = Exception.class)
    public JwtDto signup(SignupRequest signupRequest) throws BaseException {
        try {
            if(!signupRequest.password().equals(signupRequest.passwordCheck())) throw new BaseException(UNMATCHED_PASSWORD);

            User newUser = signupRequest.toUser(encoder.encode(signupRequest.password()));
            userRepository.save(newUser);
            return authService.generateToken(newUser);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
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
    public void logout(Long userIdx) throws BaseException {
        try {
            User user = userRepository.findByUserIdxAndStatusEquals(userIdx, ACTIVE)
                    .orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            authService.logout(user);
            user.logout(); // LOGOUT
            userRepository.save(user);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // accessToken 재발급
    @Transactional(rollbackFor = Exception.class)
    public JwtDto reissueToken(ReissueTokenRequest reissueTokenRequest) throws BaseException {
        try {
            User user = userRepository.findByLoginIdAndStatusEquals(reissueTokenRequest.loginId(), ACTIVE)
                    .orElseThrow(() -> new BaseException(INVALID_LOGIN_ID));
            authService.validateRefreshToken(reissueTokenRequest);
            return authService.generateToken(user);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 회원 탈퇴
    @Transactional(rollbackFor = Exception.class)
    public void signout(Long userIdx) throws BaseException {
        try{
            User user = userRepository.findByUserIdxAndStatusEquals(userIdx, ACTIVE)
                    .orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            authService.signout(user);
            user.signout(); // INACTIVE
            userRepository.save(user);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 마이페이지 조회
    public UserProfileResponse getMyPage() throws BaseException {
        try {
            Long userIdx = authService.getUserIdx();
            User user = userRepository.findByUserIdxAndStatusEquals(userIdx, ACTIVE).orElseThrow(() -> new BaseException(INVALID_LOGIN_ID));

            List<UserProfileResponse.Produce> produceList = produceRepository.findTop4ByUserAndStatusEqualsOrderByCreatedDateDesc(user, ACTIVE)
                    .stream()
                    .map(produce -> new UserProfileResponse.Produce(
                            produce.getTitle(),
                            produce.getProduceImage()))
                    .collect(Collectors.toList());

            List<UserProfileResponse.Recipe> recipeList = recipeRepository.findTop4ByUserAndStatusEqualsOrderByCreatedDateDesc(user, ACTIVE)
                    .stream()
                    .map(recipe -> new UserProfileResponse.Recipe(
                            recipe.getTitle(),
                            recipe.getRecipeImage()))
                    .collect(Collectors.toList());

            List<UserProfileResponse.Giveaway> giveawayList = giveawayRepository.findTop4ByUserAndStatusEqualsOrderByCreatedDateDesc(user, ACTIVE)
                    .stream()
                    .map(giveaway -> new UserProfileResponse.Giveaway(
                            giveaway.getTitle(),
                            giveaway.getGiveawayImage()))
                    .collect(Collectors.toList());

            return new UserProfileResponse(user.getNickname(), user.getProfileImage(), produceList, recipeList, giveawayList);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
