package org.dive2025.qdeep.domain.board.dto.response;

import org.dive2025.qdeep.domain.board.entity.Board;

import java.util.List;

public record BoardListResponse(List<Board> boardList) {
}
