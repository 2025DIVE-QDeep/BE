package org.dive2025.qdeep.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserRankingInfo(
        @Schema(description = "닉네임",example = "test1")
        String nickname,
        @Schema(description = "첫 리뷰갯수",example = "99")
        Integer amountOfFirst,
        @Schema(description = "일반 리뷰갯수",example = "23")
        Integer amountOfReview) {
}
