package com.kkobugi.puremarket.comment.application;

import com.kkobugi.puremarket.comment.domain.dto.CommentEditRequest;
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

import static com.kkobugi.puremarket.common.constants.Constant.INACTIVE;
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

    // [작성자] 댓글 수정
    @Transactional(rollbackFor = Exception.class)
    public void editComment(Long commentIdx, CommentEditRequest commentEditRequest) throws BaseException {
        try {
            Comment comment = commentRepository.findById(commentIdx).orElseThrow(() -> new BaseException(INVALID_COMMENT_IDX));
            User writer = userRepository.findByUserIdx(getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            validateWriter(writer, comment);

            if (commentEditRequest.content() != null) {
                if (!commentEditRequest.content().equals("") && !commentEditRequest.content().equals(" "))
                    comment.modifyContent(commentEditRequest.content());
                else throw new BaseException(BLANK_COMMENT_CONTENT);
            }
            commentRepository.save(comment);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // [작성자] 댓글 삭제
    public void deleteComment(Long commentIdx) throws BaseException {
        try {
            Comment comment = commentRepository.findById(commentIdx).orElseThrow(() -> new BaseException(INVALID_COMMENT_IDX));
            User writer = userRepository.findByUserIdx(getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            validateWriter(writer, comment);

            comment.delete();
            commentRepository.save(comment);
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

    private static void validateWriter(User user, Comment comment) throws BaseException {
        if (!comment.getUser().equals(user)) throw new BaseException(NO_COMMENT_WRITER);
        if (comment.getStatus().equals(INACTIVE)) throw new BaseException(ALREADY_DELETED_COMMENT);
    }
}
