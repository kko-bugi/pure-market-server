package com.kkobugi.puremarket.user;

import com.kkobugi.puremarket.common.BaseException;
import com.kkobugi.puremarket.user.application.UserService;
import com.kkobugi.puremarket.user.domain.dto.SignupRequest;
import com.kkobugi.puremarket.user.domain.entity.User;
import com.kkobugi.puremarket.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
    @DisplayName("회원가입")
    public void signup() throws BaseException {

        // given
        SignupRequest signupRequest = new SignupRequest("nickname", "id", "pw", "pw", "01012345678");

        // when
        userService.signup(signupRequest);

        // then
        User user = userRepository.findByLoginId("id").orElseThrow(() -> new RuntimeException("User not found"));
        assertEquals("nickname", user.getNickname());
        assertTrue(encoder.matches("pw", user.getPassword()));
        assertEquals("01012345678", user.getContact());
    }
}
