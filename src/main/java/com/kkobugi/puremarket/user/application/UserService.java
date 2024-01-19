package com.kkobugi.puremarket.user.application;

import com.kkobugi.puremarket.common.BaseException;
import com.kkobugi.puremarket.user.domain.dto.SignupRequest;
import com.kkobugi.puremarket.user.domain.dto.SignupResponse;
import com.kkobugi.puremarket.user.domain.entity.User;
import com.kkobugi.puremarket.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.kkobugi.puremarket.common.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final BCryptPasswordEncoder encoder;
    private final UserRepository userRepository;
    private final AuthService authService;

    // 회원가입
    public SignupResponse signup(SignupRequest signupRequest) throws BaseException {
        try {
            if(checkLoginId(signupRequest.loginId())) throw new BaseException(DUPLICATED_LOGIN_ID);
            if(checkNickname(signupRequest.nickname())) throw new BaseException(DUPLICATED_NICKNAME);
            if(!signupRequest.password().equals(signupRequest.passwordCheck())) throw new BaseException(UNMATCHED_PASSWORD);

            User newUser = signupRequest.toUser(encoder.encode(signupRequest.password()));
            String accessToken = authService.createToken(newUser);
            userRepository.save(newUser);

            return new SignupResponse(accessToken);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    // 아이디 중복 체크
    public boolean checkLoginId(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }

    // 닉네임 중복 체크
    public boolean checkNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }
}
