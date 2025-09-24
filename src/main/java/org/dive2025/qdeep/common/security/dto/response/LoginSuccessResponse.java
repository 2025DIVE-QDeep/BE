package org.dive2025.qdeep.common.security.dto.response;

public record LoginSuccessResponse(String accessToken,
                                   String refreshToken,
                                   String username) {
}
