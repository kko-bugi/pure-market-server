package com.kkobugi.puremarket.recipe.application;

import com.kkobugi.puremarket.common.BaseException;
import com.kkobugi.puremarket.common.gcs.GCSService;
import com.kkobugi.puremarket.ingredient.domain.entity.Ingredient;
import com.kkobugi.puremarket.ingredient.repository.IngredientRepository;
import com.kkobugi.puremarket.recipe.domain.dto.*;
import com.kkobugi.puremarket.recipe.domain.entity.Recipe;
import com.kkobugi.puremarket.recipe.domain.entity.RecipeDescription;
import com.kkobugi.puremarket.recipe.repository.RecipeDescriptionRepository;
import com.kkobugi.puremarket.recipe.repository.RecipeRepository;
import com.kkobugi.puremarket.user.application.AuthService;
import com.kkobugi.puremarket.user.domain.entity.User;
import com.kkobugi.puremarket.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.kkobugi.puremarket.common.constants.Constant.ACTIVE;
import static com.kkobugi.puremarket.common.constants.Constant.INACTIVE;
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
    private final UserRepository userRepository;
    private final GCSService gcsService;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    // 레시피글 목록 조회
    public RecipeListResponse getRecipeList() throws BaseException {
        try {
            List<RecipeListResponse.RecipeDto> recipeDtoList = recipeRepository.findByStatusEqualsOrderByCreatedDateDesc(ACTIVE).stream()
                    .map(recipe -> new RecipeListResponse.RecipeDto(
                            recipe.getRecipeIdx(),
                            recipe.getTitle(),
                            recipe.getRecipeImage(),
                            recipe.getUser().getNickname(),
                            recipe.getUser().getProfileImage())).toList();
            return new RecipeListResponse(recipeDtoList);
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 레시피글 상세 조회
    public RecipeResponse getRecipe(Long recipeIdx) throws BaseException {
        try {
            Long userIdx = authService.getUserIdx();
            Recipe recipe = recipeRepository.findById(recipeIdx).orElseThrow(() -> new BaseException(INVALID_RECIPE_IDX));
            if (recipe.getStatus().equals(INACTIVE)) throw new BaseException(ALREADY_DELETED_RECIPE);

            boolean isWriter = false;
            if (userIdx != null && recipe.getUser() != null) {
                isWriter = userIdx.equals(recipe.getUser().getUserIdx());
            }

            List<IngredientDto> ingredientList = getIngredientList(recipe);
            List<SauceDto> sauceList = getSauceList(recipe);
            List<RecipeDescriptionDto> recipeDescriptionList = getRecipeDescriptionList(recipe);

            return new RecipeResponse(recipe.getRecipeIdx(), recipe.getTitle(), recipe.getContent(), recipe.getRecipeImage(),
                                        ingredientList, sauceList, recipeDescriptionList,
                                            recipe.getUser().getNickname(), recipe.getUser().getContact(), recipe.getUser().getProfileImage(), isWriter);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 레시피글 등록
    @Transactional(rollbackFor = Exception.class)
    public void postRecipe(MultipartFile image, RecipePostRequest recipePostRequest) throws BaseException {
        try {
            Long userIdx = getUserIdxWithValidation();
            User writer = userRepository.findByUserIdx(userIdx).orElseThrow(() -> new BaseException(INVALID_USER_IDX));

            // upload image
            String fullPath = gcsService.uploadImage("recipe", image);
            String recipeImageUrl = "https://storage.googleapis.com/"+bucketName+"/"+fullPath;

            Recipe recipe = new Recipe(writer, recipePostRequest.title(), recipePostRequest.content(), recipeImageUrl);
            recipeRepository.save(recipe);

            // 재료 리스트 생성
            List<Ingredient> ingredientList = recipePostRequest.ingredientList().stream()
                    .map(ingredientDto -> new Ingredient(recipe, ingredientDto.name(), ingredientDto.quantity(), INGREDIENT)).toList();
            ingredientRepository.saveAll(ingredientList);

            // 양념 리스트 생성
            List<Ingredient> sauceList = recipePostRequest.sauceList().stream()
                    .map(sauceDto -> new Ingredient(recipe, sauceDto.name(), sauceDto.quantity(), SAUCE)).toList();
            ingredientRepository.saveAll(sauceList);

            // 조리 순서 생성
            List<RecipeDescription> descriptionList = recipePostRequest.recipeDescriptionList().stream()
                    .map(descriptionDto -> new RecipeDescription(recipe, descriptionDto.orderNumber(), descriptionDto.description())).toList();
            recipeDescriptionRepository.saveAll(descriptionList);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 레시피글 삭제
    @Transactional(rollbackFor = Exception.class)
    public void deleteRecipe(Long recipeIdx) throws BaseException {
        try {
            Long userIdx = getUserIdxWithValidation();
            User user = userRepository.findByUserIdx(userIdx).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            Recipe recipe = recipeRepository.findById(recipeIdx).orElseThrow(() -> new BaseException(INVALID_RECIPE_IDX));

            validateWriter(user, recipe);
            recipe.delete();
            boolean isDeleted = gcsService.deleteImage(recipe.getRecipeImage());
            if (!isDeleted) throw new BaseException(IMAGE_DELETE_FAIL);
            recipeRepository.save(recipe);

            deleteRecipeDescriptions(recipe);
            deleteIngredients(recipe);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    private void deleteRecipeDescriptions(Recipe recipe) {
        List<RecipeDescription> recipeDescriptionList = recipeDescriptionRepository.findByRecipeAndStatusEquals(recipe, ACTIVE);
        for (RecipeDescription description : recipeDescriptionList) {
            description.delete();
            recipeDescriptionRepository.save(description);
        }
    }

    private void deleteIngredients(Recipe recipe) {
        List<Ingredient> ingredientList = ingredientRepository.findByRecipeAndStatusEquals(recipe, ACTIVE);
        for (Ingredient ingredient : ingredientList) {
            ingredient.delete();
            ingredientRepository.save(ingredient);
        }
    }

    // [작성자] 레시피글 수정 화면 조회
    public RecipeEditViewResponse getRecipeEditView(Long recipeIdx) throws BaseException {
        try {
            Recipe recipe = recipeRepository.findById(recipeIdx).orElseThrow(() -> new BaseException(INVALID_RECIPE_IDX));
            if (recipe.getStatus().equals(INACTIVE)) throw new BaseException(ALREADY_DELETED_GIVEAWAY);

            User user = userRepository.findByUserIdx(getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            validateWriter(user, recipe);

            List<IngredientDto> ingredientList = getIngredientList(recipe);
            List<SauceDto> sauceList = getSauceList(recipe);
            List<RecipeDescriptionDto> recipeDescriptionList = getRecipeDescriptionList(recipe);

            return new RecipeEditViewResponse(recipe.getTitle(), recipe.getContent(), recipe.getRecipeImage(),
                    ingredientList, sauceList, recipeDescriptionList,
                    recipe.getUser().getNickname(), recipe.getUser().getContact(), recipe.getUser().getProfileImage());
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // [작성자] 레시피글 수정
    @Transactional(rollbackFor = Exception.class)
    public void editRecipe(Long recipeIdx, MultipartFile image, RecipeEditRequest recipeEditRequest) throws BaseException {
        try {
            Recipe recipe = recipeRepository.findById(recipeIdx).orElseThrow(() -> new BaseException(INVALID_RECIPE_IDX));
            if (recipe.getStatus().equals(INACTIVE)) throw new BaseException(ALREADY_DELETED_RECIPE);

            User user = userRepository.findByUserIdx(getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            validateWriter(user, recipe);

            if (recipeEditRequest.title() != null) {
                if (!recipeEditRequest.title().equals("") && !recipeEditRequest.title().equals(" "))
                    recipe.modifyTitle(recipeEditRequest.title());
                else throw new BaseException(BLANK_RECIPE_TITLE);
            }
            if (recipeEditRequest.content() != null) {
                if (!recipeEditRequest.content().equals("") && !recipeEditRequest.content().equals(" "))
                    recipe.modifyContent(recipeEditRequest.content());
                else throw new BaseException(BLANK_RECIPE_CONTENT);
            }
            if (recipeEditRequest.ingredientList() != null && recipeEditRequest.sauceList() != null) {
                deleteIngredients(recipe);

                List<Ingredient> ingredientList = recipeEditRequest.ingredientList().stream()
                        .map(ingredientDto -> new Ingredient(recipe, ingredientDto.name(), ingredientDto.quantity(), INGREDIENT)).toList();
                List<Ingredient> sauceList = recipeEditRequest.sauceList().stream()
                        .map(sauceDto -> new Ingredient(recipe, sauceDto.name(), sauceDto.quantity(), SAUCE)).toList();
                ingredientRepository.saveAll(ingredientList);
                ingredientRepository.saveAll(sauceList);
            }
            if (recipeEditRequest.recipeDescriptionList() != null) {
                deleteRecipeDescriptions(recipe);

                List<RecipeDescription> descriptionList = recipeEditRequest.recipeDescriptionList().stream()
                        .map(descriptionDto -> new RecipeDescription(recipe, descriptionDto.orderNumber(), descriptionDto.description())).toList();
                recipeDescriptionRepository.saveAll(descriptionList);
            }
            if (image != null) {
                // delete previous image
                boolean isDeleted = gcsService.deleteImage(recipe.getRecipeImage());
                if (!isDeleted) throw new BaseException(IMAGE_DELETE_FAIL);

                // upload new image
                String fullPath = gcsService.uploadImage("recipe", image);
                String newImageUrl = "https://storage.googleapis.com/"+bucketName+"/"+fullPath;
                recipe.modifyImage(newImageUrl);
            } else throw new BaseException(NULL_RECIPE_IMAGE);
            recipeRepository.save(recipe);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    private List<RecipeDescriptionDto> getRecipeDescriptionList(Recipe recipe) {
        return recipeDescriptionRepository.findByRecipeAndStatusEqualsOrderByCreatedDateDesc(recipe, ACTIVE).stream()
                .map(description -> new RecipeDescriptionDto(
                        description.getOrderNumber(),
                        description.getDescription())).toList();
    }

    private List<SauceDto> getSauceList(Recipe recipe) {
        return ingredientRepository.findByRecipeAndIngredientTypeAndStatusEqualsOrderByCreatedDateDesc(recipe, SAUCE, ACTIVE).stream()
                .map(sauce -> new SauceDto(
                        sauce.getName(),
                        sauce.getQuantity())).toList();
    }

    private List<IngredientDto> getIngredientList(Recipe recipe) {
        return ingredientRepository.findByRecipeAndIngredientTypeAndStatusEqualsOrderByCreatedDateDesc(recipe, INGREDIENT, ACTIVE).stream()
                .map(ingredient -> new IngredientDto(
                        ingredient.getName(),
                        ingredient.getQuantity())).toList();
    }

    private static void validateWriter(User user, Recipe recipe) throws BaseException {
        if (!recipe.getUser().equals(user)) throw new BaseException(NO_RECIPE_WRITER);
        if (recipe.getStatus().equals(INACTIVE)) throw new BaseException(ALREADY_DELETED_RECIPE);
    }

    // 비회원 예외처리
    private Long getUserIdxWithValidation() throws BaseException {
        Long userIdx = authService.getUserIdx();
        if (userIdx == null) throw new BaseException(NULL_ACCESS_TOKEN);
        return userIdx;
    }
}
