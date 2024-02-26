package com.kkobugi.puremarket.comment.application;

import com.kkobugi.puremarket.comment.domain.dto.CommentPostRequest;
import com.kkobugi.puremarket.comment.domain.entity.Comment;
import com.kkobugi.puremarket.comment.repository.CommentRepository;
import com.kkobugi.puremarket.common.BaseException;
import com.kkobugi.puremarket.giveaway.domain.entity.Giveaway;
import com.kkobugi.puremarket.giveaway.repository.GiveawayRepository;
import com.kkobugi.puremarket.produce.domain.entity.Produce;
import com.kkobugi.puremarket.produce.repository.ProduceRepository;
import com.kkobugi.puremarket.recipe.domain.entity.Recipe;
import com.kkobugi.puremarket.recipe.repository.RecipeRepository;
import com.kkobugi.puremarket.user.application.AuthService;
import com.kkobugi.puremarket.user.domain.entity.User;
import com.kkobugi.puremarket.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.kkobugi.puremarket.common.enums.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ProduceRepository produceRepository;
    private final GiveawayRepository giveawayRepository;
    private final RecipeRepository recipeRepository;

    // 댓글 등록
    @Transactional(rollbackFor = Exception.class)
    public void postComment(CommentPostRequest commentPostRequest) throws BaseException {
        try {
            User writer = userRepository.findByUserIdx(getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));

            if (commentPostRequest.produceIdx() != null) {
                Produce produce = produceRepository.findById(commentPostRequest.produceIdx()).orElseThrow(() -> new BaseException(INVALID_PRODUCE_IDX));
                Comment comment = Comment.builder()
                        .user(writer)
                        .produce(produce)
                        .giveaway(null)
                        .recipe(null)
                        .content(commentPostRequest.content())
                        .build();
                commentRepository.save(comment);
            } else if (commentPostRequest.giveawayIdx() != null) {
                Giveaway giveaway = giveawayRepository.findById(commentPostRequest.giveawayIdx()).orElseThrow(() -> new BaseException(INVALID_GIVEAWAY_IDX));
                Comment comment = Comment.builder()
                        .user(writer)
                        .produce(null)
                        .giveaway(giveaway)
                        .recipe(null)
                        .content(commentPostRequest.content())
                        .build();
                commentRepository.save(comment);
            } else {
                Recipe recipe = recipeRepository.findById(commentPostRequest.recipeIdx()).orElseThrow(() -> new BaseException(INVALID_RECIPE_IDX));
                Comment comment = Comment.builder()
                        .user(writer)
                        .produce(null)
                        .giveaway(null)
                        .recipe(recipe)
                        .content(commentPostRequest.content())
                        .build();
                commentRepository.save(comment);
            }
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    private Long getUserIdxWithValidation() throws BaseException {
        Long userIdx = authService.getUserIdx();
        if (userIdx == null) throw new BaseException(NULL_ACCESS_TOKEN);
        return userIdx;
    }
}
