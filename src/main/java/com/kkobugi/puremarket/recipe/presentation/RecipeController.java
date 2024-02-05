package com.kkobugi.puremarket.recipe.presentation;

import com.kkobugi.puremarket.common.BaseException;
import com.kkobugi.puremarket.common.BaseResponse;
import com.kkobugi.puremarket.recipe.application.RecipeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.kkobugi.puremarket.common.constants.RequestURI.recipe;

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
}
