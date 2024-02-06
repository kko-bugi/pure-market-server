package com.kkobugi.puremarket.home.application;

import com.kkobugi.puremarket.common.BaseException;
import com.kkobugi.puremarket.giveaway.repository.GiveawayRepository;
import com.kkobugi.puremarket.home.domain.dto.HomeResponse;
import com.kkobugi.puremarket.produce.repository.ProduceRepository;
import com.kkobugi.puremarket.recipe.repository.RecipeRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.kkobugi.puremarket.common.constants.Constant.ACTIVE;
import static com.kkobugi.puremarket.common.constants.Constant.Giveaway.GIVEAWAY;
import static com.kkobugi.puremarket.common.constants.Constant.Produce.FOR_SALE;
import static com.kkobugi.puremarket.common.enums.BaseResponseStatus.DATABASE_ERROR;

@Service
@RequiredArgsConstructor
public class HomeService {
    private final ProduceRepository produceRepository;
    private final RecipeRepository recipeRepository;
    private final GiveawayRepository giveawayRepository;

    public HomeResponse getHome() throws BaseException {
        try {
            List<HomeResponse.ProduceDto> produceList = produceRepository.findTop4ByStatusEqualsOrderByCreatedDateDesc(FOR_SALE).stream()
                    .map(produce -> new HomeResponse.ProduceDto(
                            produce.getProduceIdx(),
                            produce.getTitle(),
                            produce.getPrice(),
                            produce.getProduceImage())).toList();

            List<HomeResponse.RecipeDto> recipeList = recipeRepository.findTop4ByStatusEqualsOrderByCreatedDateDesc(ACTIVE).stream()
                    .map(recipe -> new HomeResponse.RecipeDto(
                            recipe.getRecipeIdx(),
                            recipe.getTitle(),
                            recipe.getRecipeImage())).toList();

            List<HomeResponse.GiveawayDto> giveawayList = giveawayRepository.findTop4ByStatusEqualsOrderByCreatedDateDesc(GIVEAWAY).stream()
                    .map(giveaway -> new HomeResponse.GiveawayDto(
                            giveaway.getGiveawayIdx(),
                            giveaway.getTitle(),
                            giveaway.getContent(),
                            giveaway.getGiveawayImage())).toList();

            return new HomeResponse(produceList, recipeList, giveawayList);
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
