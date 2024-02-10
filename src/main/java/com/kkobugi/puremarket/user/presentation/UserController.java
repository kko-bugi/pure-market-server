package com.kkobugi.puremarket.user.presentation;

import com.kkobugi.puremarket.common.BaseException;
import com.kkobugi.puremarket.common.BaseResponse;
import com.kkobugi.puremarket.user.application.AuthService;
import com.kkobugi.puremarket.user.application.UserService;
import com.kkobugi.puremarket.user.domain.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.kkobugi.puremarket.common.constants.RequestURI.user;
import static com.kkobugi.puremarket.common.enums.BaseResponseStatus.SUCCESS;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer")
@RequestMapping(user)
@Tag(name = "User", description = "유저 API")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

   // 회원가입
    @PostMapping(value = "/signup", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "회원가입", description = "유저 회원가입을 수행한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "회원가입 성공"),
            @ApiResponse(responseCode = "2001", description = "이미 사용중인 아이디", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "2002", description = "이미 사용중인 닉네임", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "2003", description = "비밀번호 불일치", content = @Content(schema = @Schema(implementation = BaseResponse.class)))})
    public BaseResponse<JwtDto> signup(@RequestPart(value = "image", required = false) MultipartFile image, @RequestPart(value = "signupRequest") SignupRequest signupRequest) {
        try {
            return new BaseResponse<>(userService.signup(image, signupRequest));
        } catch(BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 로그인
    @PostMapping("/login")
    @Operation(summary = "로그인", description = "유저 로그인을 수행한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "로그인 성공"),
            @ApiResponse(responseCode = "2000", description = "잘못된 userIdx", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "2004", description = "잘못된 비밀번호", content = @Content(schema = @Schema(implementation = BaseResponse.class)))})
    public BaseResponse<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            return new BaseResponse<>(userService.login(loginRequest));
        } catch(BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 로그아웃
    @PatchMapping("/logout")
    @Operation(summary = "로그아웃", description = "유저 로그아웃을 수행한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "로그아웃 성공"),
            @ApiResponse(responseCode = "2000", description = "잘못된 userIdx", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "2003", description = "잘못된 비밀번호", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "2006", description = "잘못된 Access Token", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "2008", description = "빈 Access Token", content = @Content(schema = @Schema(implementation = BaseResponse.class)))})
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
    @Operation(summary = "닉네임 중복 체크", description = "닉네임 중복체크를 수행한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "중복 체크 성공"),
            @ApiResponse(responseCode = "2002", description = "이미 사용중인 닉네임", content = @Content(schema = @Schema(implementation = BaseResponse.class)))})
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
    @Operation(summary = "아이디 중복 체크", description = "아이디 중복체크를 수행한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "중복 체크 성공"),
            @ApiResponse(responseCode = "2001", description = "이미 사용중인 아이디", content = @Content(schema = @Schema(implementation = BaseResponse.class)))})
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
    @Operation(summary = "Access Token 재발급", description = "액세스 토큰을 재발급한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "재발급 성공"),
            @ApiResponse(responseCode = "2007", description = "잘못된 refresh token", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "3000", description = "존재하지 않는 user", content = @Content(schema = @Schema(implementation = BaseResponse.class)))})
    public BaseResponse<JwtDto> reissueToken(@RequestBody ReissueTokenRequest reissueTokenRequest) {
        try{
            return new BaseResponse<>(userService.reissueToken(reissueTokenRequest));
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 회원 탈퇴
    @PatchMapping("/signout")
    @Operation(summary = "회원 탈퇴", description = "유저 탈퇴를 진행한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "탈퇴 성공"),
            @ApiResponse(responseCode = "2000", description = "잘못된 userIdx", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "2006", description = "잘못된 Access Token", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "2008", description = "빈 Access Token", content = @Content(schema = @Schema(implementation = BaseResponse.class)))})
    public BaseResponse<?> signOut() {
        try{
            userService.signOut(authService.getUserIdx());
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 마이페이지 조회
    @GetMapping("/my-page")
    @Operation(summary = "마이페이지 조회", description = "마이페지지를 조회한다. 작성한 글 목록이 비었을 경우 예외처리 없이 빈 리스트로 반환한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "조회 성공"),
            @ApiResponse(responseCode = "2000", description = "잘못된 userIdx", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "2008", description = "빈 Access Token", content = @Content(schema = @Schema(implementation = BaseResponse.class)))})
    public BaseResponse<MyPageResponse> getMyPage() {
        try{
            return new BaseResponse<>(userService.getMyPage());
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 회원 정보 조회
    @GetMapping("/profile")
    @Operation(summary = "유저 프로필 조회", description = "유저 프로필을 조회한다. 글 작성 페이지에서 자동으로 유저 정보를 조회할 때 사용한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "조회 성공"),
            @ApiResponse(responseCode = "2000", description = "잘못된 userIdx", content = @Content(schema = @Schema(implementation = BaseResponse.class)))})
    public BaseResponse<ProfileResponse> getProfile() {
        try{
            return new BaseResponse<>(userService.getProfile());
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
}
