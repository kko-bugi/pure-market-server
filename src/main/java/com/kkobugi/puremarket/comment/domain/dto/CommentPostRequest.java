package com.kkobugi.puremarket.comment.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;

@Schema(description = "댓글 등록 요청")
public record CommentPostRequest(String content,
                                 Long produceIdx,
                                 Long giveawayIdx,
                                 Long recipeIdx) {
    @AssertTrue(message = "댓글은 recipe, produce, giveaway 중 하나에만 속해야 한다.")
    public boolean isOnlyOneNotNull() {
        int count = 0;
        if (produceIdx != null) count++;
        if (giveawayIdx != null) count++;
        if (recipeIdx != null) count++;

        return count == 1;
    }
}
