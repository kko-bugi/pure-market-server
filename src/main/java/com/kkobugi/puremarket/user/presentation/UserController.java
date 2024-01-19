package com.kkobugi.puremarket.user.presentation;

import com.kkobugi.puremarket.common.BaseException;
import com.kkobugi.puremarket.common.BaseResponse;
import com.kkobugi.puremarket.user.application.UserService;
import com.kkobugi.puremarket.user.domain.dto.SignupRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.kkobugi.puremarket.common.BaseResponseStatus.*;
import static com.kkobugi.puremarket.common.constants.RequestURI.user;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer")
@RequestMapping(user)
@Tag(name = "User", description = "User API")
public class UserController {

    private final UserService userService;

    /**
     * [POST] 회원가입
     * @param signupRequest
     */
    @PostMapping("/signup")
    @Operation(summary = "회원가입")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "회원가입 성공"),
            @ApiResponse(responseCode = "2001", description = "아이디 중복"),
            @ApiResponse(responseCode = "2002", description = "닉네임 중복"),
            @ApiResponse(responseCode = "2003", description = "비밀번호 불일치")})
    public BaseResponse<?> signup(@RequestBody SignupRequest signupRequest) {
        try {
            userService.signup(signupRequest);
            return new BaseResponse(SUCCESS);
        } catch(BaseException e) {
            return new BaseResponse(e.getStatus());
        }
    }

}
