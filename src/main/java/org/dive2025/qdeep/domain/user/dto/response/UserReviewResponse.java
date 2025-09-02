package org.dive2025.qdeep.domain.user.dto.response;

import org.dive2025.qdeep.domain.board.entity.Board;

import java.util.List;

public record UserReviewResponse(List<Board> board) {
}
