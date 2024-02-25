package com.kkobugi.puremarket.recipe.presentation;

import com.kkobugi.puremarket.common.BaseException;
import com.kkobugi.puremarket.common.BaseResponse;
import com.kkobugi.puremarket.recipe.application.RecipeService;
import com.kkobugi.puremarket.recipe.domain.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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

import static com.kkobugi.puremarket.common.constants.RequestURI.recipe;
import static com.kkobugi.puremarket.common.enums.BaseResponseStatus.SUCCESS;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer")
@RequestMapping(recipe)
@Tag(name = "Recipe", description = "레시피글 API")
public class RecipeController {

    private final RecipeService recipeService;

    // 레시피글 목록 조회
    @GetMapping("")
    @Operation(summary = "레시피글 목록 조회", description = "레시피글 전체 목록을 조회한다. 글 목록이 비었을 시 예외처리 없이 빈 리스트를 반환한다.")
    @ApiResponses(value = {@ApiResponse(responseCode = "1000", description = "조회 성공")})
    public BaseResponse<RecipeListResponse> getRecipeList() {
        try {
            return new BaseResponse<>(recipeService.getRecipeList());
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 레시피글 상세 조회
    @GetMapping("/{recipeIdx}")
    @Operation(summary = "레시피글 상세 조회", description = "레시피글의 상세 페이지를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "조회 성공"),
            @ApiResponse(responseCode = "2008", description = "빈 access token", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "2200", description = "잘못된 레시피글 Idx", content = @Content(schema = @Schema(implementation = BaseResponse.class)))})
    public BaseResponse<RecipeResponse> getRecipe(@Parameter(description = "레시피글 Idx", in = ParameterIn.PATH) @PathVariable Long recipeIdx) {
        try {
            return new BaseResponse<>(recipeService.getRecipe(recipeIdx));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 레시피글 등록
    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "레시피글 등록", description = "레시피글을 작성한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "등록 성공"),
            @ApiResponse(responseCode = "2000", description = "잘못된 userIdx", content = @Content(schema = @Schema(implementation = BaseResponse.class)))})
    public BaseResponse<?> postRecipe(@RequestPart(value = "image", required = false) MultipartFile image, @RequestPart(value = "recipeRequest") RecipePostRequest recipePostRequest) {
        try {
            recipeService.postRecipe(image, recipePostRequest);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // [작성자] 레시피글 삭제
    @PatchMapping("/{recipeIdx}")
    @Operation(summary = "레시피글 삭제", description = "레시피글을 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "삭제 성공"),
            @ApiResponse(responseCode = "2000", description = "잘못된 userIdx", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "2200", description = "잘못된 레시피글 Idx", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "2201", description = "레시피글 작성자가 아닙니다.", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "2202", description = "이미 삭제된 레시피글", content = @Content(schema = @Schema(implementation = BaseResponse.class)))})
    public BaseResponse<?> deleteRecipe(@Parameter(description = "레시피글 Idx", in = ParameterIn.PATH) @PathVariable Long recipeIdx) {
        try {
            recipeService.deleteRecipe(recipeIdx);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // [작성자] 레시피글 수정 화면 조회
    @GetMapping("/editView/{recipeIdx}")
    @Operation(summary = "레시피글 수정화면 조회", description = "해당 레시피글의 원데이터를 조회한다.")
    public BaseResponse<RecipeEditViewResponse> getRecipeEditView(@Parameter(description = "레시피글 Idx", in = ParameterIn.PATH) @PathVariable Long recipeIdx) {
        try {
            return new BaseResponse<>(recipeService.getRecipeEditView(recipeIdx));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // [작성자] 레시피글 수정
    @PatchMapping("/edit/{recipeIdx}")
    @Operation(summary = "레시피글 수정", description = "레시피글을 수정한다.")
    public BaseResponse<?> editRecipe(@Parameter(description = "레시피글 Idx", in = ParameterIn.PATH) @PathVariable Long recipeIdx,
                                       @RequestPart(value = "image", required = false) MultipartFile image,
                                       @RequestPart(value = "recipeRequest") RecipeEditRequest recipeEditRequest) {
        try {
            recipeService.editRecipe(recipeIdx, image, recipeEditRequest);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
