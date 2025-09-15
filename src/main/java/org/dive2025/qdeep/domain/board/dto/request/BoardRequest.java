package org.dive2025.qdeep.domain.board.dto.request;

public record BoardRequest(String content,
                           Long userId,
                           Long storeId) {
}
