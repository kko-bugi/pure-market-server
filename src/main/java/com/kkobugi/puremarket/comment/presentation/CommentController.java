package com.kkobugi.puremarket.comment.presentation;

import com.kkobugi.puremarket.comment.application.CommentService;
import com.kkobugi.puremarket.comment.domain.dto.CommentEditRequest;
import com.kkobugi.puremarket.comment.domain.dto.CommentPostRequest;
import com.kkobugi.puremarket.common.BaseException;
import com.kkobugi.puremarket.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.kkobugi.puremarket.common.constants.RequestURI.comment;
import static com.kkobugi.puremarket.common.enums.BaseResponseStatus.SUCCESS;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer")
@RequestMapping(comment)
@Tag(name = "Comment", description = "댓글 API")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("")
    @Operation(summary = "댓글 등록", description = "특정글에 댓글을 등록한다.")
    public BaseResponse<?> postComment(@Valid @RequestBody CommentPostRequest commentPostRequest) {
        try {
            commentService.postComment(commentPostRequest);
            return new BaseResponse<>(SUCCESS);
        } catch(BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PatchMapping("/edit/{commentIdx}")
    @Operation(summary = "댓글 수정", description = "댓글을 수정한다.")
    public BaseResponse<?> editComment(@Parameter(description = "댓글 Idx", in = ParameterIn.PATH) @PathVariable Long commentIdx,
                                      @RequestPart(value = "commentRequest") CommentEditRequest commentEditRequest) {
        try {
            commentService.editComment(commentIdx, commentEditRequest);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PatchMapping("/delete/{commentIdx}")
    @Operation(summary = "댓글 삭제", description = "댓글을 삭제한다.")
    public BaseResponse<?> deleteComment(@Parameter(description = "댓글 Idx", in = ParameterIn.PATH) @PathVariable Long commentIdx) {
        try {
            commentService.deleteComment(commentIdx);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
