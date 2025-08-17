package org.dive2025.qdeep.common.security.dto.response;

public record LogoutSuccessResponse(boolean success,
                                    String message,
                                    String timeStamp) {
}
