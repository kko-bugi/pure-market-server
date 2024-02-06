package com.kkobugi.puremarket.recipe.presentation;

import com.kkobugi.puremarket.common.BaseException;
import com.kkobugi.puremarket.common.BaseResponse;
import com.kkobugi.puremarket.recipe.application.RecipeService;
import com.kkobugi.puremarket.recipe.domain.dto.RecipePostRequest;
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
    public BaseResponse<?> getRecipeList() {
        try {
            return new BaseResponse<>(recipeService.getRecipeList());
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 레시피글 상세 조회
    @GetMapping("/{recipeIdx}")
    public BaseResponse<?> getRecipe(@PathVariable Long recipeIdx) {
        try {
            return new BaseResponse<>(recipeService.getRecipe(recipeIdx));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 레시피글 등록
    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public BaseResponse<?> postRecipe(@RequestPart(value = "image", required = false) MultipartFile image, @RequestPart(value = "recipeRequest") RecipePostRequest recipePostRequest) {
        try {
            recipeService.postRecipe(image, recipePostRequest);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
