package com.kkobugi.puremarket.recipe.application;

import com.kkobugi.puremarket.common.BaseException;
import com.kkobugi.puremarket.ingredient.repository.IngredientRepository;
import com.kkobugi.puremarket.recipe.domain.dto.RecipeListResponse;
import com.kkobugi.puremarket.recipe.domain.dto.RecipeResponse;
import com.kkobugi.puremarket.recipe.domain.entity.Recipe;
import com.kkobugi.puremarket.recipe.repository.RecipeDescriptionRepository;
import com.kkobugi.puremarket.recipe.repository.RecipeRepository;
import com.kkobugi.puremarket.user.application.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.kkobugi.puremarket.common.constants.Constant.ACTIVE;
import static com.kkobugi.puremarket.common.enums.BaseResponseStatus.*;
import static com.kkobugi.puremarket.common.enums.IngredientType.INGREDIENT;
import static com.kkobugi.puremarket.common.enums.IngredientType.SAUCE;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final AuthService authService;
    private final IngredientRepository ingredientRepository;
    private final RecipeDescriptionRepository recipeDescriptionRepository;

    // 레시피글 목록 조회
    public RecipeListResponse getRecipeList() throws BaseException {
        try {
            // TODO: 글 없을 때 예외처리 여부 결정 후 수정
            List<RecipeListResponse.RecipeDto> recipeDtoList = recipeRepository.findByStatusEqualsOrderByCreatedDateDesc(ACTIVE).stream()
                    .map(recipe -> new RecipeListResponse.RecipeDto(
                            recipe.getRecipeIdx(),
                            recipe.getTitle(),
                            recipe.getRecipeImage(),
                            recipe.getUser().getNickname(),
                            recipe.getUser().getProfileImage())).toList();
            return new RecipeListResponse(recipeDtoList);
//        } catch (BaseException e) {
//            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 레시피글 상세 조회
    public RecipeResponse getRecipe(Long recipeIdx) throws BaseException {
        try {
            Long userIdx = authService.getUserIdxFromToken();
            Recipe recipe = recipeRepository.findById(recipeIdx).orElseThrow(() -> new BaseException(INVALID_RECIPE_IDX));

            boolean isWriter = false;
            if (userIdx != null && recipe.getUser() != null) {
                isWriter = userIdx.equals(recipe.getUser().getUserIdx());
            }

            // 재료 리스트
            List<RecipeResponse.IngredientDto> ingredientList = ingredientRepository.findByRecipeAndIngredientTypeAndStatusEqualsOrderByCreatedDateDesc(recipe, INGREDIENT, ACTIVE).stream()
                    .map(ingredient -> new RecipeResponse.IngredientDto(
                            ingredient.getName(),
                            ingredient.getQuantity())).toList();

            // 양념 리스트
            List<RecipeResponse.SauceDto> sauceList = ingredientRepository.findByRecipeAndIngredientTypeAndStatusEqualsOrderByCreatedDateDesc(recipe, SAUCE, ACTIVE).stream()
                    .map(sauce -> new RecipeResponse.SauceDto(
                            sauce.getName(),
                            sauce.getQuantity())).toList();

            // 레시피 상세 리스트(조리 순서)
            List<RecipeResponse.RecipeDescriptionDto> recipeDescriptionList = recipeDescriptionRepository.findByRecipeAndStatusEqualsOrderByCreatedDateDesc(recipe, ACTIVE).stream()
                    .map(description -> new RecipeResponse.RecipeDescriptionDto(
                            description.getOrderNumber(),
                            description.getDescription())).toList();

            return new RecipeResponse(recipe.getRecipeIdx(), recipe.getTitle(), recipe.getContent(), recipe.getRecipeImage(),
                                        ingredientList, sauceList, recipeDescriptionList,
                                            recipe.getUser().getNickname(), recipe.getUser().getContact(), recipe.getUser().getProfileImage(), isWriter);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
