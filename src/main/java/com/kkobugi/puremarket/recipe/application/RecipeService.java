package com.kkobugi.puremarket.recipe.application;

import com.kkobugi.puremarket.common.BaseException;
import com.kkobugi.puremarket.recipe.domain.dto.RecipeListResponse;
import com.kkobugi.puremarket.recipe.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.kkobugi.puremarket.common.constants.Constant.ACTIVE;
import static com.kkobugi.puremarket.common.enums.BaseResponseStatus.DATABASE_ERROR;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;

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
}
