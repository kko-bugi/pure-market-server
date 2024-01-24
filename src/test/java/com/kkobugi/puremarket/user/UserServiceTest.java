package com.kkobugi.puremarket.user;

import com.kkobugi.puremarket.common.BaseException;
import com.kkobugi.puremarket.user.application.UserService;
import com.kkobugi.puremarket.user.domain.dto.LoginRequest;
import com.kkobugi.puremarket.user.domain.dto.LoginResponse;
import com.kkobugi.puremarket.user.domain.dto.SignupRequest;
import com.kkobugi.puremarket.user.domain.entity.User;
import com.kkobugi.puremarket.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static com.kkobugi.puremarket.common.enums.BaseResponseStatus.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Test
    @Transactional
    @DisplayName("회원가입")
    public void signup() throws BaseException {

        // given
        SignupRequest signupRequest = new SignupRequest("nickname", "id", "pw", "pw", "01012345678");

        // when
        userService.signup(signupRequest);

        // then
        User user = userRepository.findByLoginId("id").orElseThrow(() -> new BaseException(INVALID_LOGIN_ID));
        assertEquals("nickname", user.getNickname());
        assertTrue(encoder.matches("pw", user.getPassword()));
        assertEquals("01012345678", user.getContact());
    }

    @Test
    @Transactional
    @DisplayName("로그인")
    public void login() throws BaseException {

        // given
        signup();
        LoginRequest loginRequest = new LoginRequest("id", "pw");

        // when
        LoginResponse loginResponse = userService.login(loginRequest);

        // then
        User user = userRepository.findByLoginId("id").orElseThrow(() -> new BaseException(INVALID_LOGIN_ID));
        assertNotNull(loginResponse.accessToken());
        assertTrue(encoder.matches("pw", user.getPassword()));
        assertEquals("id", user.getLoginId());
    }

    @Test
    @Transactional
    @DisplayName("닉네임 중복 체크")
    public void check_duplicated_nickname() throws BaseException {

        // given
        SignupRequest signupRequest1 = new SignupRequest("nickname", "id1", "pw", "pw", "01012345678");
        SignupRequest signupRequest2 = new SignupRequest("nickname", "id2", "pw", "pw", "01012345679");

        // when
        userService.signup(signupRequest1);

        // then
        assertThrows(BaseException.class, () -> userService.signup(signupRequest2), DUPLICATED_NICKNAME.getMessage());
    }

    @Test
    @Transactional
    @DisplayName("아이디 중복 체크")
    public void check_duplicated_loginId() throws BaseException {
        // given
        SignupRequest signupRequest1 = new SignupRequest("nickname1", "id", "pw", "pw", "01012345678");
        SignupRequest signupRequest2 = new SignupRequest("nickname2", "id", "pw", "pw", "01012345679");

        // when
        userService.signup(signupRequest1);

        // then
        assertThrows(BaseException.class, () -> userService.signup(signupRequest2) ,DUPLICATED_LOGIN_ID.getMessage());
    }
}
