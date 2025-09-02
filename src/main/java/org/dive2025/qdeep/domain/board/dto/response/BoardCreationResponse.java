package org.dive2025.qdeep.domain.board.dto.response;

public record BoardCreationResponse(String content,
                                    String nickname,
                                    String creationTime,
                                    String storeName,
                                    String address,
                                    String hours) {
}
