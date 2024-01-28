package com.kkobugi.puremarket.user.presentation;

import com.kkobugi.puremarket.common.BaseException;
import com.kkobugi.puremarket.common.BaseResponse;
import com.kkobugi.puremarket.user.application.AuthService;
import com.kkobugi.puremarket.user.application.UserService;
import com.kkobugi.puremarket.user.domain.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.kkobugi.puremarket.common.constants.RequestURI.user;
import static com.kkobugi.puremarket.common.enums.BaseResponseStatus.SUCCESS;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer")
@RequestMapping(user)
@Tag(name = "User", description = "User API")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

   // 회원가입
    @PostMapping("/signup")
    @Operation(summary = "회원가입")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "회원가입 성공"),
            @ApiResponse(responseCode = "2001", description = "아이디 중복"),
            @ApiResponse(responseCode = "2002", description = "닉네임 중복"),
            @ApiResponse(responseCode = "2003", description = "비밀번호 불일치")})
    public BaseResponse<?> signup(SignupRequest signupRequest) { // 이미지 업로드 위해 @RequestBody 제거
        try {
            return new BaseResponse<>(userService.signup(signupRequest));
        } catch(BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 로그인
    @PostMapping("/login")
    public BaseResponse<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            return new BaseResponse<>(userService.login(loginRequest));
        } catch(BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 로그아웃
    @PatchMapping("/logout")
    public BaseResponse<?> logout() {
        try{
            userService.logout(authService.getUserIdx());
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 닉네임 중복 체크
    @PostMapping("/nickname")
    public BaseResponse<?> validateNickname(@RequestBody NicknameRequest nicknameRequest) {
        try {
            userService.validateNickname(nicknameRequest.nickname());
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 아이디 중복 체크
    @PostMapping("/loginId")
    public BaseResponse<?> validateLoginId(@RequestBody LoginIdRequest loginIdRequest) {
        try {
            userService.validateLoginId(loginIdRequest.loginId());
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    // accessToken 재발급
    @PostMapping("/reissue-token")
    public BaseResponse<?> reissueToken(@RequestBody ReissueTokenRequest reissueTokenRequest) {
        try{
            return new BaseResponse<>(userService.reissueToken(reissueTokenRequest));
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 회원 탈퇴
    @PatchMapping("/signout")
    public BaseResponse<?> signout() {
        try{
            userService.signout(authService.getUserIdx());
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 마이페이지 조회
    @GetMapping("/my-page")
    public BaseResponse<?> getMyPage() {
        try{
            return new BaseResponse<>(userService.getMyPage());
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
}
